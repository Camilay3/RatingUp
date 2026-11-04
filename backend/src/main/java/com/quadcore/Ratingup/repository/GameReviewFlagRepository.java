package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.enums.ReviewFlagStatus;
import com.quadcore.Ratingup.model.game.GameReviewFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameReviewFlagRepository extends JpaRepository<GameReviewFlag, Long> {
    boolean existsByGameIdAndUserId(Long gameId, Long userId);

    List<GameReviewFlag> findByStatusOrderByCreatedAtDesc(ReviewFlagStatus status);

    long countByUserIdAndStatus(Long userId, ReviewFlagStatus status);
}