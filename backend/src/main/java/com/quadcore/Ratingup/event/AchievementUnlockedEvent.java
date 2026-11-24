package com.quadcore.Ratingup.event;

import com.quadcore.Ratingup.model.achievement.Achievement;
import com.quadcore.Ratingup.model.profile.User;

public record AchievementUnlockedEvent(
        User user,
        Achievement achievement
) {
}
