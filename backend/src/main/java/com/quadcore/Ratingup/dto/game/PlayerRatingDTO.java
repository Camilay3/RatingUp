package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.SkillLevel;
import com.quadcore.Ratingup.enums.TimeControlCategory;

public record PlayerRatingDTO(
        TimeControlCategory category,
        int rating,
        int gamesPlayed,
        int wins,
        int losses,
        int draws,
        Integer performanceRating,
        SkillLevel skillLevel
) {}