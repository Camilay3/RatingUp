package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.achievement.Achievement;
import com.quadcore.Ratingup.model.profile.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserId(Long userId);
    Optional<UserAchievement> findByUserIdAndAchievementId(Long userId, Long achievementId);
    List<UserAchievement> findByUserIdAndIsCompletedTrue(Long userId);
}
