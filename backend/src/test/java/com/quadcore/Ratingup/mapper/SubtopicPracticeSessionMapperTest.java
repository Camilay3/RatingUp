package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.model.board.SubtopicPracticeSession;
import com.quadcore.Ratingup.model.book.Subtopics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtopicPracticeSessionMapperTest {

    @Test
    @DisplayName("Should map to ResponseDTO correctly")
    void testToDTO() {
        SubtopicPracticeSession session = new SubtopicPracticeSession();
        session.setId(1L);
        session.setCurrentFen("fen1");
        session.setStatus(BoardStatus.NORMAL);

        Subtopics subtopic = new Subtopics();
        subtopic.setInitialFen("initial-fen");

        SubtopicPracticeSessionResponseDTO dto = SubtopicPracticeSessionMapper.toDTO(session, subtopic);

        assertNotNull(dto);
        assertEquals(1L, dto.sessionId());
        assertEquals("fen1", dto.fen());
        assertEquals("NORMAL", dto.status());
        assertEquals("initial-fen", dto.initialFen());
    }

    @Test
    @DisplayName("Should create new Session correctly")
    void testToNewSession() {
        Subtopics subtopic = new Subtopics();
        subtopic.setInitialFen("initial-fen");

        SubtopicPracticeSession session = SubtopicPracticeSessionMapper.toNewSession(1L, 2L, subtopic);

        assertNotNull(session);
        assertEquals(1L, session.getUserId());
        assertEquals(2L, session.getSubtopicId());
        assertEquals("initial-fen", session.getCurrentFen());
        assertEquals(BoardStatus.NORMAL, session.getStatus());
    }
}
