package com.quadcore.Ratingup.model.game;

import com.quadcore.Ratingup.enums.ReviewFlagStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_review_flags",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_review_flag_game_user", columnNames = {"game_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class GameReviewFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int score;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reasons;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewFlagStatus status = ReviewFlagStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameReviewFlag)) return false;
        GameReviewFlag other = (GameReviewFlag) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}