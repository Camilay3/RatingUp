package com.quadcore.Ratingup.model.game;
//olhar as implementações no lugar errado do claude
import com.quadcore.Ratingup.dto.game.TimeControlDTO;
import com.quadcore.Ratingup.enums.GameEndReason;
import com.quadcore.Ratingup.enums.GameResult;
import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.enums.TimeControlCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.bhlangonijr.chesslib.Side;
import java.time.Duration;

import java.time.LocalDateTime;

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

    @Column(columnDefinition = "TEXT")
    private String moveTimesMs;

    private int moveNumber;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(nullable = false)
    private int initialTimeSeconds;

    @Column(nullable = false)
    private int incrementSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeControlCategory timeControlCategory;

    @Enumerated(EnumType.STRING)
    private GameResult result;

    @Enumerated(EnumType.STRING)
    private GameEndReason endReason;

    private LocalDateTime lastMoveAt;
    private LocalDateTime finishedAt;

    private Integer whiteRatingBefore;
    private Integer blackRatingBefore;
    private Integer whiteRatingChange;
    private Integer blackRatingChange;

    @Column(nullable = false)
    private boolean flaggedForReview;

    @Version
    private Long version;

    public TimeControlDTO timeControl() {
        return new TimeControlDTO(initialTimeSeconds, incrementSeconds);
    }

    public void applyTimeControl(TimeControlDTO tc) {
        this.initialTimeSeconds = tc.initialSeconds();
        this.incrementSeconds = tc.incrementSeconds();
        this.timeControlCategory = tc.category();
    }

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

    /**
     * Tempo restante de um lado, em ms. Fonte de verdade do relógio.
     * Consumido = soma do tempo gasto nos lances do lado; cada lance devolve o incremento.
     * Para quem está com a vez, desconta também o tempo corrente desde o último lance.
     */
    public long remainingMs(Side side, LocalDateTime now) {
        long remaining = initialTimeSeconds * 1000L;

        if (moveTimesMs != null && !moveTimesMs.isBlank()) {
            String[] spent = moveTimesMs.split(",");
            for (int i = 0; i < spent.length; i++) {
                // índice par = lance das brancas
                if ((i % 2 == 0) == (side == Side.WHITE)) {
                    remaining += incrementSeconds * 1000L - Long.parseLong(spent[i].trim());
                }
            }
        }

        if (side == sideToMove()) {
            if (status == GameStatus.IN_PROGRESS && lastMoveAt != null) {
                remaining -= Math.max(0, Duration.between(lastMoveAt, now).toMillis());
            } else if (status == GameStatus.FINISHED && endReason == GameEndReason.TIMEOUT) {
                remaining = 0; // quem estava com a vez perdeu por tempo
            }
        }
        return Math.max(0, remaining);
    }

    // a partida sempre começa da posição inicial: lance par = vez das brancas
    private Side sideToMove() {
        return moveNumber % 2 == 0 ? Side.WHITE : Side.BLACK;
    }
}