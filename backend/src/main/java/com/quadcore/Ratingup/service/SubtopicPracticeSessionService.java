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

import java.time.LocalDateTime;
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

        return toDTO(subtopicPracticeSessionRepository.save(session));
    }


    public SubtopicPracticeSessionResponseDTO performMovement(Long sessionId, SubtopicPracticeSessionRequestDTO moveDto) {
        SubtopicPracticeSession session = subtopicPracticeSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        Board board = new Board();
        board.loadFromFen(session.getCurrentFen());

        Move move = new Move(moveDto.posInitial(), moveDto.posFinal(), moveDto.piece());

        if (!board.legalMoves().contains(move)) {
            throw new IllegalArgumentException("Movimento inválido!");
        }

        board.doMove(move);

        session.setCurrentFen(board.getFen());
        session.setStatus(BoardStatus.valueOf(checkStatus(board)));
        subtopicPracticeSessionRepository.save(session);

        return new SubtopicPracticeSessionResponseDTO(board.getFen(), checkStatus(board));
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
    //colocar isso e as coias de cima num mapper
    private SubtopicPracticeSessionResponseDTO toDTO(SubtopicPracticeSession session) {
        return new SubtopicPracticeSessionResponseDTO(session.getCurrentFen(), session.getStatus().name());
    }

    //ver se deixa isso aq mesmo
    public SubtopicPracticeSessionResponseDTO getSession(Long sessionId) {
        SubtopicPracticeSession session = subtopicPracticeSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
        return toDTO(session);
    }

}