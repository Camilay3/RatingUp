package com.quadcore.Ratingup.model.game;

import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.model.profile.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long whitePlayerId;
    private Long blackPlayerId;

    @Column(length = 100, nullable = false)
    private String currentFen;

    @Column(columnDefinition = "TEXT")
    private String movesPlayed;

    private int moveNumber;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game other = (Game) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
