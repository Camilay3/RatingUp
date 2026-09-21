package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionRequestDTO;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.dto.board.SubtopicTypeResponseDto;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.enums.SubtopicType;
import com.quadcore.Ratingup.model.board.SubtopicPracticeSession;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.repository.SubtopicPracticeSessionRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtopicPracticeSessionServiceTest {

    @Mock
    private SubtopicsRepository subtopicsRepository;

    @Mock
    private SubtopicPracticeSessionRepository sessionRepository;

    @InjectMocks
    private SubtopicPracticeSessionService service;

    private Subtopics subtopic;
    private SubtopicPracticeSession session;

    @BeforeEach
    void setUp() {
        subtopic = new Subtopics();
        subtopic.setId(1L);
        subtopic.setInitialFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        subtopic.setSolutionMoves("e2e4");
        subtopic.setOpponentMoves("e7e5");
        subtopic.setType(SubtopicType.BOARD);

        session = new SubtopicPracticeSession();
        session.setId(1L);
        session.setUserId(1L);
        session.setSubtopicId(1L);
        session.setCurrentFen(subtopic.getInitialFen());
        session.setStatus(BoardStatus.NORMAL);
    }

    @Test
    @DisplayName("Should start session")
    void testStartSession() {
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));
        when(sessionRepository.findByUserIdAndSubtopicId(1L, 1L)).thenReturn(Optional.empty());
        when(sessionRepository.save(any())).thenReturn(session);

        SubtopicPracticeSessionResponseDTO dto = service.startSession(1L, 1L);

        assertNotNull(dto);
        assertEquals(1L, dto.sessionId());
        verify(sessionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should start session and delete existing")
    void testStartSession_DeleteExisting() {
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));
        when(sessionRepository.findByUserIdAndSubtopicId(1L, 1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any())).thenReturn(session);

        SubtopicPracticeSessionResponseDTO dto = service.startSession(1L, 1L);

        assertNotNull(dto);
        verify(sessionRepository, times(1)).delete(any());
        verify(sessionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get subtopic type")
    void testGetSubtopicType() {
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));

        SubtopicTypeResponseDto dto = service.getSubtopicType(1L);

        assertNotNull(dto);
        assertEquals(SubtopicType.BOARD, dto.type());
    }

    @Test
    @DisplayName("Should perform movement successfully")
    void testPerformMovement_Success() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));
        when(sessionRepository.save(any())).thenReturn(session);

        SubtopicPracticeSessionRequestDTO request = new SubtopicPracticeSessionRequestDTO(
                1L, Piece.WHITE_PAWN, Square.E2, Square.E4
        );

        SubtopicPracticeSessionResponseDTO dto = service.performMovement(request);

        assertNotNull(dto);
        verify(sessionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should perform movement with wrong move")
    void testPerformMovement_WrongMove() {
        subtopic.setSolutionMoves("d2d4");
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));
        when(sessionRepository.save(any())).thenReturn(session);

        SubtopicPracticeSessionRequestDTO request = new SubtopicPracticeSessionRequestDTO(
                1L, Piece.WHITE_PAWN, Square.E2, Square.E4
        );

        SubtopicPracticeSessionResponseDTO dto = service.performMovement(request);

        assertNotNull(dto);
        assertEquals("WRONG_MOVE", dto.status());
    }
    @Test
    @DisplayName("Should perform movement with opponent move")
    void testPerformMovement_OpponentMove() {
        subtopic.setSolutionMoves("e2e4,g1f3");
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));
        when(sessionRepository.save(any())).thenReturn(session);

        SubtopicPracticeSessionRequestDTO request = new SubtopicPracticeSessionRequestDTO(
                1L, Piece.WHITE_PAWN, Square.E2, Square.E4
        );

        SubtopicPracticeSessionResponseDTO dto = service.performMovement(request);

        assertNotNull(dto);
        assertEquals("NORMAL", dto.status());
    }

    @Test
    @DisplayName("Should perform movement resulting in checkmate")
    void testPerformMovement_Checkmate() {
        // Fools mate FEN
        subtopic.setInitialFen("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3");
        subtopic.setSolutionMoves("h4h5"); // Just some valid move for the test that continues it
        session.setCurrentFen(subtopic.getInitialFen());
        
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));
        when(sessionRepository.save(any())).thenReturn(session);

        // Even though it's already checkmate, any check on status will return CHECKMATE. 
        // Wait, the board will be loaded from current fen, then we apply the move. 
        // Let's use a fen 1 move BEFORE checkmate.
        // FEN: rnbqkbnr/ppppp2p/5p2/6p1/4P3/3P4/PPP2PPP/RNBQKBNR w KQkq - 0 3
        subtopic.setInitialFen("rnbqkbnr/pppp1ppp/8/4p3/5PP1/8/PPPPP2P/RNBQKBNR b KQkq - 0 2");
        subtopic.setSolutionMoves("d8h4,a2a3"); // Black moves Qh4#
        session.setCurrentFen(subtopic.getInitialFen());
        
        SubtopicPracticeSessionRequestDTO request = new SubtopicPracticeSessionRequestDTO(
                1L, Piece.BLACK_QUEEN, Square.D8, Square.H4
        );

        SubtopicPracticeSessionResponseDTO dto = service.performMovement(request);

        assertNotNull(dto);
        assertEquals("CHECKMATE", dto.status());
    }
}
