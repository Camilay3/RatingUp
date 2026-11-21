package com.quadcore.Ratingup.dto.achievement;

import com.quadcore.Ratingup.enums.AchievementCategory;
import com.quadcore.Ratingup.model.achievement.Achievement;

public record AchievementResponseDTO(
        Long id,
        String code,
        String title,
        String description,
        AchievementCategory category,
        int targetValue,
        int rewardExperience,
        String rewardTitle
) {
    public AchievementResponseDTO (Achievement achievement){
        this(
                achievement.getId(),
                achievement.getCode(),
                achievement.getTitle(),
                achievement.getDescription(),
                achievement.getCategory(),
                achievement.getTargetValue(),
                achievement.getRewardExperience(),
                achievement.getRewardTitle()
        );
    }
}
