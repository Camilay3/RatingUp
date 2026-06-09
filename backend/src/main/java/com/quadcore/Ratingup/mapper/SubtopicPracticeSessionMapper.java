package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.model.board.SubtopicPracticeSession;
import com.quadcore.Ratingup.model.book.Subtopics;

public abstract class SubtopicPracticeSessionMapper {
    public static SubtopicPracticeSessionResponseDTO toDTO(SubtopicPracticeSession session, Subtopics subtopic) {
        return new SubtopicPracticeSessionResponseDTO(
                session.getCurrentFen(),
                session.getStatus().name(),
                subtopic.getInitialFen()
        );
    }

    public static SubtopicPracticeSession toNewSession(Long userId, Long subtopicId, Subtopics subtopic) {
        SubtopicPracticeSession session = new SubtopicPracticeSession();
        session.setUserId(userId);
        session.setSubtopicId(subtopicId);
        session.setCurrentFen(subtopic.getInitialFen());
        session.setStatus(BoardStatus.NORMAL);
        return session;
    }
}
