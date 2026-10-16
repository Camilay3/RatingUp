package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.game.*;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.exception.BusinessRuleViolationException;
import com.quadcore.Ratingup.exception.ResourceNotFoundException;
import com.quadcore.Ratingup.exception.UnauthorizedOperationException;
import com.quadcore.Ratingup.mapper.GameMapper;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.GameRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameEventPublisher publisher;

    @Transactional
    public GameStateDTO createGame(Long userId) {
        // se já existe uma partida minha aguardando adversário, devolve a mesma
        Optional<Game> waiting = gameRepository.findByPlayerAndStatusNot(userId, GameStatus.FINISHED)
                .stream()
                .filter(g -> g.getStatus() == GameStatus.WAITING)
                .findFirst();
        if (waiting.isPresent()) {
            return buildState(waiting.get(), userId);
        }

        Game game = new Game();
        game.setWhitePlayerId(userId); // provisório: as cores são sorteadas quando alguém entra
        game.setCurrentFen(new Board().getFen());
        game.setMoveNumber(0);
        game.setStatus(GameStatus.WAITING);
        gameRepository.save(game);

        return buildState(game, userId);
    }

    @Transactional
    public GameStateDTO joinGame(Long gameId, Long userId) {
        Game game = findGame(gameId);

        if (game.getStatus() != GameStatus.WAITING) {
            throw new BusinessRuleViolationException("Esta partida não está mais aberta", "GAME_NOT_OPEN");
        }
        if (userId.equals(game.getWhitePlayerId())) {
            throw new BusinessRuleViolationException(
                    "Você não pode entrar na sua própria partida", "CANNOT_JOIN_OWN_GAME");
        }

        if (ThreadLocalRandom.current().nextBoolean()) {
            game.setBlackPlayerId(game.getWhitePlayerId());
            game.setWhitePlayerId(userId);
        } else {
            game.setBlackPlayerId(userId);
        }
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);

        publishAfterCommit(game.getId(), GameMapper.toStartedEvent(game));
        return buildState(game, userId);
    }

    @Transactional
    public void cancelGame(Long gameId, Long userId) {
        Game game = findGame(gameId);

        if (game.getStatus() != GameStatus.WAITING) {
            throw new BusinessRuleViolationException(
                    "Só é possível cancelar partidas aguardando adversário", "GAME_NOT_CANCELLABLE");
        }
        if (!userId.equals(game.getWhitePlayerId())) {
            throw new UnauthorizedOperationException("Só quem criou a partida pode cancelá-la");
        }
        gameRepository.delete(game);
    }

    @Transactional(readOnly = true)
    public List<GameSummaryDTO> listOpenGames(Long userId) {
        return gameRepository
                .findTop20ByStatusAndWhitePlayerIdNotOrderByIdDesc(GameStatus.WAITING, userId)
                .stream()
                .map(g -> GameMapper.toSummary(g, null, nickname(g.getWhitePlayerId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GameSummaryDTO> listMyGames(Long userId) {
        return gameRepository.findByPlayerAndStatusNot(userId, GameStatus.FINISHED)
                .stream()
                .map(g -> {
                    Side mine = sideOf(g, userId);
                    Long opponentId = mine == Side.WHITE ? g.getBlackPlayerId() : g.getWhitePlayerId();
                    return GameMapper.toSummary(g, mine, opponentId == null ? null : nickname(opponentId));
                })
                .toList();
    }

    @Transactional
    public GameEventDTO makeMove(Long gameId, Long playerId, GameMoveResquestDTO req) {
        Game game = findGame(gameId);
        Side playerSide = sideOf(game, playerId);

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new BusinessRuleViolationException(
                    "A partida não está em andamento", "GAME_NOT_IN_PROGRESS");
        }

        Board board = new Board();
        board.loadFromFen(game.getCurrentFen());

        if (board.getSideToMove() != playerSide) {
            throw new BusinessRuleViolationException("Não é a sua vez", "NOT_YOUR_TURN");
        }

        String uci = (req.from() + req.to()
                + (req.promotion() == null ? "" : req.promotion())).toLowerCase();

        Move move = board.legalMoves().stream()
                .filter(m -> m.toString().equals(uci))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException(
                        "Movimento inválido", "ILLEGAL_MOVE"));

        board.doMove(move);

        BoardStatus boardStatus = statusOf(board);

        game.setCurrentFen(board.getFen());
        game.setMoveNumber(game.getMoveNumber() + 1);
        game.setMovesPlayed(game.getMovesPlayed() == null
                ? uci
                : game.getMovesPlayed() + "," + uci);

        if (boardStatus == BoardStatus.CHECKMATE || boardStatus == BoardStatus.DRAW) {
            game.setStatus(GameStatus.FINISHED);
        }
        gameRepository.save(game);

        GameEventDTO event = GameMapper.toEvent(game, move, board.getSideToMove(), boardStatus);
        publishAfterCommit(game.getId(), event);
        return event;
    }

    @Transactional(readOnly = true)
    public GameStateDTO getState(Long gameId, Long userId) {
        return buildState(findGame(gameId), userId);
    }

    public boolean isParticipant(Long gameId, Long userId) {
        return gameRepository.isParticipant(gameId, userId);
    }

    private GameStateDTO buildState(Game game, Long userId) {
        Side mySide = sideOf(game, userId);

        Board board = new Board();
        board.loadFromFen(game.getCurrentFen());

        Long opponentId = mySide == Side.WHITE ? game.getBlackPlayerId() : game.getWhitePlayerId();
        String opponent = opponentId == null ? null : nickname(opponentId);

        return GameMapper.toState(game, board.getSideToMove(), mySide, statusOf(board), opponent);
    }

    private BoardStatus statusOf(Board board) {
        if (board.isMated()) return BoardStatus.CHECKMATE;
        if (board.isDraw()) return BoardStatus.DRAW;
        if (board.isKingAttacked()) return BoardStatus.CHECK;
        return BoardStatus.NORMAL;
    }

    private void publishAfterCommit(Long gameId, GameEventDTO event) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publisher.publish(gameId, event);
            }
        });
    }

    private String nickname(Long userId) {
        return userRepository.findById(userId).map(User::getNickname).orElse("Jogador");
    }

    private Game findGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada"));
    }

    private Side sideOf(Game game, Long userId) {
        if (userId.equals(game.getWhitePlayerId())) return Side.WHITE;
        if (userId.equals(game.getBlackPlayerId())) return Side.BLACK;
        throw new UnauthorizedOperationException("Você não participa desta partida");
    }
}