package com.quadcore.Ratingup.dto.achievement;

import com.quadcore.Ratingup.model.achievement.Achievement;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.model.profile.UserAchievement;

import java.time.LocalDateTime;

public record UserAchievementResponseDTO(
        Long achievementId,
        String code,
        String Title,
        String description,
        int currentProgress,
        int targetValue,
        boolean isCompleted,
        LocalDateTime completedAt
) {
    public UserAchievementResponseDTO(UserAchievement userAchievement){
        this(
                userAchievement.getAchievement().getId(),
                userAchievement.getAchievement().getCode(),
                userAchievement.getAchievement().getTitle(),
                userAchievement.getAchievement().getDescription(),
                userAchievement.getCurrentProgress(),
                userAchievement.getAchievement().getTargetValue(),
                userAchievement.isCompleted(),
                userAchievement.getCompletedAt()
        );
    }
}
