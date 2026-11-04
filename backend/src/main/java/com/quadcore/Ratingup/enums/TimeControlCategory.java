package com.quadcore.Ratingup.enums;

public enum TimeControlCategory {
    BULLET(180),
    BLITZ(600),
    RAPID(3600),
    CLASSICAL(Integer.MAX_VALUE);

    private final int upperBoundSeconds;

    TimeControlCategory(int upperBoundSeconds) {
        this.upperBoundSeconds = upperBoundSeconds;
    }

    public static TimeControlCategory fromEstimatedDuration(int seconds) {
        for (TimeControlCategory c : values()) {
            if (seconds < c.upperBoundSeconds) return c;
        }
        return CLASSICAL;
    }
}