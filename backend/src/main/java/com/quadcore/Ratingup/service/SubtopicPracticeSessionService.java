package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.board.*;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.mapper.SubtopicPracticeSessionMapper;
import com.quadcore.Ratingup.model.board.MultipleChoiceOption;
import com.quadcore.Ratingup.model.board.MultipleChoiceQuestion;
import com.quadcore.Ratingup.model.board.SubtopicPracticeSession;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.repository.SubtopicPracticeSessionRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SubtopicPracticeSessionService {

    @Autowired
    private SubtopicsRepository subtopicsRepository;

    @Autowired
    private SubtopicPracticeSessionRepository subtopicPracticeSessionRepository;

    public SubtopicPracticeSessionResponseDTO startSession(Long userId, Long subtopicId) {
        Subtopics subtopic = subtopicsRepository.findById(subtopicId)
                .orElseThrow(() -> new EntityNotFoundException("Subtópico não encontrado"));

        subtopicPracticeSessionRepository
                .findByUserIdAndSubtopicId(userId, subtopicId)
                .ifPresent(subtopicPracticeSessionRepository::delete);

        SubtopicPracticeSession session = SubtopicPracticeSessionMapper.toNewSession(userId, subtopicId, subtopic);

        return SubtopicPracticeSessionMapper.toDTO(subtopicPracticeSessionRepository.save(session), subtopic);
    }

    public SubtopicPracticeSessionResponseDTO performMovement(SubtopicPracticeSessionRequestDTO moveDto) {
        SubtopicPracticeSession session = subtopicPracticeSessionRepository.findById(moveDto.sessionId())
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        Subtopics subtopic = subtopicsRepository.findById(session.getSubtopicId())
                .orElseThrow(() -> new EntityNotFoundException("Subtópico não encontrado"));

        Board board = new Board();
        board.loadFromFen(session.getCurrentFen());

        Move move = new Move(moveDto.posInitial(), moveDto.posFinal(), moveDto.piece());

        if (!board.legalMoves().contains(move)) {
            throw new IllegalArgumentException("Movimento inválido!");
        }

        board.doMove(move);

        //transforma ambas sequencias de jogada em lista pra comparar
        String moveNotation = moveDto.posInitial().toString().toLowerCase()
                + moveDto.posFinal().toString().toLowerCase();
        String movesPlayed = session.getMovesPlayed() == null
                ? moveNotation
                : session.getMovesPlayed() + "," + moveNotation;
        session.setMovesPlayed(movesPlayed);

        List<String> solution = Arrays.asList(subtopic.getSolutionMoves().split(","));
        List<String> played = Arrays.asList(movesPlayed.split(","));

        // Movimento errado,reinicia
        if (!solution.get(played.size() - 1).equals(played.get(played.size() - 1))) {
            session.setCurrentFen(subtopic.getInitialFen());
            session.setMovesPlayed(null);
            session.setStatus(BoardStatus.WRONG_MOVE);
            return SubtopicPracticeSessionMapper.toDTO(subtopicPracticeSessionRepository.save(session), subtopic);
        }

        // Sequência completa,sucesso
        if (played.equals(solution)) {
            session.setCurrentFen(board.getFen());
            session.setStatus(BoardStatus.COMPLETED);
            return SubtopicPracticeSessionMapper.toDTO(subtopicPracticeSessionRepository.save(session), subtopic);
        }

        // Movimento correto, sequência ainda não terminou

        int opponentMoveIndex = played.size() - 1;
        executeOpponentMoveIfExists(board, subtopic, opponentMoveIndex);


        session.setCurrentFen(board.getFen());
        session.setStatus(BoardStatus.valueOf(checkStatus(board)));
        return SubtopicPracticeSessionMapper.toDTO(subtopicPracticeSessionRepository.save(session), subtopic);
    }

    private String checkStatus(Board board) {
        if (board.isMated()) {
            return BoardStatus.CHECKMATE.name();
        } else if (board.isDraw()) {
            return BoardStatus.DRAW.name();
        } else if (board.isKingAttacked()) {
            return BoardStatus.CHECK.name();
        } else {
            return BoardStatus.NORMAL.name();
        }
    }

    public SubtopicTypeResponseDto getSubtopicType(Long subtopicId) {
        Subtopics subtopic = subtopicsRepository.findById(subtopicId).orElseThrow(()->new RuntimeException("subtópico não encontrado"));
        return new SubtopicTypeResponseDto(subtopicId,subtopic.getType());
    }

    private void executeOpponentMoveIfExists(Board board, Subtopics subtopic, int index) {
        if (subtopic.getOpponentMoves() == null || subtopic.getOpponentMoves().isBlank()) return;

        String[] opponentMoves = subtopic.getOpponentMoves().split(",");
        if (index >= opponentMoves.length) return;

        String notation = opponentMoves[index].trim();
        Square from = Square.fromValue(notation.substring(0, 2).toUpperCase());
        Square to   = Square.fromValue(notation.substring(2, 4).toUpperCase());

        Move move = new Move(from, to);
        if (board.legalMoves().contains(move)) {
            board.doMove(move);
        }
    }
}