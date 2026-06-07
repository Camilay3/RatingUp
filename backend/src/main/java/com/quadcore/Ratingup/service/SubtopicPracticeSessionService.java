package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionRequestDTO;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.enums.BoardStatus;
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

        SubtopicPracticeSession session = new SubtopicPracticeSession();
        session.setUserId(userId);
        session.setSubtopicId(subtopicId);
        session.setCurrentFen(subtopic.getInitialFen());
        session.setStatus(BoardStatus.NORMAL);

        return toDTO(subtopicPracticeSessionRepository.save(session), subtopic);
    }

    public SubtopicPracticeSessionResponseDTO performMovement(Long sessionId, SubtopicPracticeSessionRequestDTO moveDto) {
        SubtopicPracticeSession session = subtopicPracticeSessionRepository.findById(sessionId)
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

        String moveNotation = moveDto.posInitial().toString().toLowerCase()
                + moveDto.posFinal().toString().toLowerCase();
        String movesPlayed = session.getMovesPlayed() == null
                ? moveNotation
                : session.getMovesPlayed() + "," + moveNotation;
        session.setMovesPlayed(movesPlayed);

        List<String> solution = Arrays.asList(subtopic.getSolutionMoves().split(","));
        List<String> played = Arrays.asList(movesPlayed.split(","));

        // Movimento errado → reinicia
        if (!solution.get(played.size() - 1).equals(played.get(played.size() - 1))) {
            session.setCurrentFen(subtopic.getInitialFen());
            session.setMovesPlayed(null);
            session.setStatus(BoardStatus.WRONG_MOVE);
            subtopicPracticeSessionRepository.save(session);
            return toDTO(session, subtopic);
        }

        // Sequência completa → sucesso
        if (played.equals(solution)) {
            session.setCurrentFen(board.getFen());
            session.setStatus(BoardStatus.COMPLETED);
            subtopicPracticeSessionRepository.save(session);
            return toDTO(session, subtopic);
        }

        // Movimento correto, sequência ainda não terminou
        session.setCurrentFen(board.getFen());
        session.setStatus(BoardStatus.valueOf(checkStatus(board)));
        subtopicPracticeSessionRepository.save(session);
        return toDTO(session, subtopic);
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

    private SubtopicPracticeSessionResponseDTO toDTO(SubtopicPracticeSession session, Subtopics subtopic) {
        return new SubtopicPracticeSessionResponseDTO(
                session.getCurrentFen(),
                session.getStatus().name(),
                subtopic.getInitialFen()
        );
    }

    private SubtopicPracticeSessionResponseDTO toDTO(SubtopicPracticeSession session) {
        Subtopics subtopic = subtopicsRepository.findById(session.getSubtopicId())
                .orElseThrow(() -> new EntityNotFoundException("Subtópico não encontrado"));
        return toDTO(session, subtopic);
    }

    public SubtopicPracticeSessionResponseDTO getSession(Long sessionId) {
        SubtopicPracticeSession session = subtopicPracticeSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
        return toDTO(session);
    }
}