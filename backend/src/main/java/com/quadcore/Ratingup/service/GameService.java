package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.game.*;
import com.quadcore.Ratingup.enums.*;
import com.quadcore.Ratingup.exception.BusinessRuleViolationException;
import com.quadcore.Ratingup.exception.ResourceNotFoundException;
import com.quadcore.Ratingup.exception.UnauthorizedOperationException;
import com.quadcore.Ratingup.mapper.GameMapper;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.dto.game.TimeControlDTO;
import com.quadcore.Ratingup.model.game.PlayerRating;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.GameRepository;
import com.quadcore.Ratingup.repository.PlayerRatingRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final PlayerRatingRepository playerRatingRepository;
    private final GameFinalizationService finalizationService;
    private final GameEventPublisher publisher;

    @Transactional
    public GameStateDTO createGame(Long userId, CreateGameRequestDTO request) {
        TimeControlDTO timeControl = request == null ? TimeControlDTO.DEFAULT : request.toTimeControl();

        // se já existe uma partida minha aguardando adversário, reaproveita quando o
        // controle de tempo é o mesmo; caso contrário descarta a antiga e cria a nova
        Optional<Game> waiting = gameRepository.findByPlayerAndStatusNot(userId, GameStatus.FINISHED)
                .stream()
                .filter(g -> g.getStatus() == GameStatus.WAITING)
                .findFirst();
        if (waiting.isPresent()) {
            Game existing = waiting.get();
            if (existing.timeControl().equals(timeControl)) {
                return buildState(existing, userId);
            }
            gameRepository.delete(existing);
        }

        Game game = new Game();
        game.setWhitePlayerId(userId); // provisório: as cores são sorteadas quando alguém entra
        game.setCurrentFen(new Board().getFen());
        game.setMoveNumber(0);
        game.setStatus(GameStatus.WAITING);
        game.applyTimeControl(timeControl);
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
        game.setLastMoveAt(LocalDateTime.now()); // início da contagem do 1º lance
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

    @Transactional(readOnly = true)
    public List<PlayerRatingDTO> listMyRatings(Long userId) {
        Map<TimeControlCategory, PlayerRating> byCategory = playerRatingRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(PlayerRating::getCategory, Function.identity()));

        // sempre devolve as 4 categorias; as nunca jogadas vêm com o rating inicial
        return Arrays.stream(TimeControlCategory.values())
                .map(c -> byCategory.containsKey(c)
                        ? GameMapper.toRatingDTO(byCategory.get(c))
                        : GameMapper.toDefaultRatingDTO(c))
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

        LocalDateTime now = LocalDateTime.now();
        if (game.remainingMs(playerSide, now) <= 0) {
            return finishByTimeout(game, board, playerSide);
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

        // tempo gasto neste lance (insumo do anti-cheat); reutiliza o "now" já capturado acima
        long spentMs = game.getLastMoveAt() == null
                ? 0 : Math.max(0, Duration.between(game.getLastMoveAt(), now).toMillis());

        game.setCurrentFen(board.getFen());
        game.setMoveNumber(game.getMoveNumber() + 1);
        game.setMovesPlayed(append(game.getMovesPlayed(), uci));
        game.setMoveTimesMs(append(game.getMoveTimesMs(), String.valueOf(spentMs)));
        game.setLastMoveAt(now);

        if (boardStatus == BoardStatus.CHECKMATE) {
            // quem acabou de mover deu o mate
            GameResult result = playerSide == Side.WHITE ? GameResult.WHITE_WINS : GameResult.BLACK_WINS;
            finalizationService.finish(game, result, GameEndReason.CHECKMATE);
        } else if (boardStatus == BoardStatus.DRAW) {
            finalizationService.finish(game, GameResult.DRAW, GameEndReason.DRAW_BY_RULE);
        } else {
            gameRepository.save(game);
        }

        GameEventDTO event = GameMapper.toEvent(game, move, board.getSideToMove(), boardStatus);
        publishAfterCommit(game.getId(), event);
        return event;
    }

    @Transactional
    public GameEventDTO resign(Long gameId, Long userId) {
        Game game = findGame(gameId);
        Side side = sideOf(game, userId); // 403 se não for participante

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new BusinessRuleViolationException(
                    "A partida não está em andamento", "GAME_NOT_IN_PROGRESS");
        }

        Board board = new Board();
        board.loadFromFen(game.getCurrentFen());

        GameResult result = side == Side.WHITE ? GameResult.BLACK_WINS : GameResult.WHITE_WINS;
        finalizationService.finish(game, result, GameEndReason.RESIGNATION);

        GameEventDTO event = GameMapper.toResignedEvent(game, board.getSideToMove(), statusOf(board));
        publishAfterCommit(game.getId(), event);
        return event;
    }

    /** Qualquer jogador da partida pode reivindicar o tempo; o servidor confere o relógio. */
    @Transactional
    public GameEventDTO claimTimeout(Long gameId, Long userId) {
        Game game = findGame(gameId);
        sideOf(game, userId); // 403 se não for participante

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new BusinessRuleViolationException(
                    "A partida não está em andamento", "GAME_NOT_IN_PROGRESS");
        }

        Board board = new Board();
        board.loadFromFen(game.getCurrentFen());
        Side turn = board.getSideToMove();

        if (game.remainingMs(turn, LocalDateTime.now()) > 0) {
            throw new BusinessRuleViolationException(
                    "O tempo do jogador ainda não esgotou", "TIME_NOT_EXPIRED");
        }
        return finishByTimeout(game, board, turn);
    }

    @Transactional(readOnly = true)
    public GameStateDTO getState(Long gameId, Long userId) {
        return buildState(findGame(gameId), userId);
    }

    public boolean isParticipant(Long gameId, Long userId) {
        return gameRepository.isParticipant(gameId, userId);
    }

    private String append(String current, String value) {
        return current == null ? value : current + "," + value;
    }

    private GameStateDTO buildState(Game game, Long userId) {
        Side mySide = sideOf(game, userId);

        Board board = new Board();
        board.loadFromFen(game.getCurrentFen());

        Long opponentId = mySide == Side.WHITE ? game.getBlackPlayerId() : game.getWhitePlayerId();
        String opponent = opponentId == null ? null : nickname(opponentId);

        TimeControlCategory category = game.getTimeControlCategory();
        Integer myRating = ratingOf(userId, category);
        Integer opponentRating = opponentId == null ? null : ratingOf(opponentId, category);

        return GameMapper.toState(game, board.getSideToMove(), mySide, statusOf(board),
                opponent, myRating, opponentRating);
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

    private int ratingOf(Long userId, TimeControlCategory category) {
        return playerRatingRepository.findByUserIdAndCategory(userId, category)
                .map(PlayerRating::getRating).orElse(PlayerRating.DEFAULT_RATING);
    }

    private GameEventDTO finishByTimeout(Game game, Board board, Side loser) {
        GameResult result = loser == Side.WHITE ? GameResult.BLACK_WINS : GameResult.WHITE_WINS;
        finalizationService.finish(game, result, GameEndReason.TIMEOUT);

        GameEventDTO event = GameMapper.toTimeoutEvent(game, board.getSideToMove(), statusOf(board));
        publishAfterCommit(game.getId(), event);
        return event;
    }
}