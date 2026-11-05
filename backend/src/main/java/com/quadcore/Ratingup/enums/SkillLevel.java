package com.quadcore.Ratingup.enums;

public enum SkillLevel {
    UNRATED, BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT, MASTER;

    public static SkillLevel fromRating(int rating) {
        if (rating < 900) return BEGINNER;
        if (rating < 1200) return NOVICE;
        if (rating < 1500) return INTERMEDIATE;
        if (rating < 1800) return ADVANCED;
        if (rating < 2100) return EXPERT;
        return MASTER;
    }
}