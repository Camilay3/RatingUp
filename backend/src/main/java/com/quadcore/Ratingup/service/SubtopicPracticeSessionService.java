package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionRequestDTO;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.mapper.SubtopicPracticeSessionMapper;
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
        System.out.println("Move criado: " + move);
        System.out.println("Legal moves: " + board.legalMoves());
        System.out.println("Contém? " + board.legalMoves().contains(move));

        //transforma ambas sequencias de jogada em lista pra comparar
        String moveNotation = moveDto.posInitial().toString().toLowerCase()
                + moveDto.posFinal().toString().toLowerCase();

        if (!board.legalMoves().stream().anyMatch(m -> m.toString().startsWith(moveNotation))) {
            throw new IllegalArgumentException("Movimento inválido!");
        }
        board.doMove(move);


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

}