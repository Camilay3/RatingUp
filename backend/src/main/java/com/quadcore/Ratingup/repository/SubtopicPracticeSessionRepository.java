package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.board.SubtopicPracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubtopicPracticeSessionRepository extends JpaRepository<SubtopicPracticeSession,Long> {
    Optional<SubtopicPracticeSession> findByUserIdAndSubtopicId(Long userId, Long subtopicId);
}
