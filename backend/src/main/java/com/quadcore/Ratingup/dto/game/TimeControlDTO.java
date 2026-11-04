package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.TimeControlCategory;

public record TimeControlDTO(int initialSeconds, int incrementSeconds) {

    public static final TimeControlDTO DEFAULT = new TimeControlDTO(600, 0); // 10+0 (Rapid)

    public int estimatedDurationSeconds() {
        return initialSeconds + 40 * incrementSeconds;
    }

    public TimeControlCategory category() {
        return TimeControlCategory.fromEstimatedDuration(estimatedDurationSeconds());
    }
}