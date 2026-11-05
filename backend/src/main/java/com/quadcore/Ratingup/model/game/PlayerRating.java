package com.quadcore.Ratingup.model.game;

import com.quadcore.Ratingup.enums.SkillLevel;
import com.quadcore.Ratingup.enums.TimeControlCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_ratings",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_player_rating_user_category", columnNames = {"user_id", "category"}))
@Getter
@Setter
@NoArgsConstructor
public class PlayerRating {

    public static final int DEFAULT_RATING = 400;
    public static final int MIN_RATING = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeControlCategory category;

    @Column(nullable = false)
    private int rating = DEFAULT_RATING;

    @Column(nullable = false)
    private int gamesPlayed;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int losses;

    @Column(nullable = false)
    private int draws;

    private Integer performanceRating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel skillLevel = SkillLevel.UNRATED;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void recordGame(int newRating, double score) {
        this.rating = newRating;
        this.gamesPlayed++;
        if (score == 1.0) wins++;
        else if (score == 0.0) losses++;
        else draws++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerRating)) return false;
        PlayerRating other = (PlayerRating) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}