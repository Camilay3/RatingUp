package com.quadcore.Ratingup.model.game;

import com.github.bhlangonijr.chesslib.Side;
import com.quadcore.Ratingup.enums.GameEndReason;
import com.quadcore.Ratingup.enums.GameStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameClockTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

    private Game game(int initial, int increment) {
        Game g = new Game();
        g.setInitialTimeSeconds(initial);
        g.setIncrementSeconds(increment);
        g.setStatus(GameStatus.IN_PROGRESS);
        g.setLastMoveAt(NOW.minusSeconds(5));
        return g;
    }

    @Test
    void onlyTheSideToMoveLosesRunningTime() {
        Game g = game(600, 0);
        assertEquals(595_000, g.remainingMs(Side.WHITE, NOW));
        assertEquals(600_000, g.remainingMs(Side.BLACK, NOW));
    }

    @Test
    void movesSpendTimeAndGiveBackTheIncrement() {
        Game g = game(600, 2);
        g.setMoveTimesMs("4000,2000,6000");
        g.setMoveNumber(3); // vez das pretas
        g.setLastMoveAt(NOW.minusSeconds(3));

        assertEquals(594_000, g.remainingMs(Side.WHITE, NOW)); // 600000 + 2*2000 - (4000+6000)
        assertEquals(597_000, g.remainingMs(Side.BLACK, NOW)); // 600000 + 2000 - 2000 - 3000
    }

    @Test
    void neverGoesBelowZero() {
        Game g = game(15, 0);
        g.setLastMoveAt(NOW.minusSeconds(20));
        assertEquals(0, g.remainingMs(Side.WHITE, NOW));
    }

    @Test
    void waitingGameKeepsFullTime() {
        Game g = game(300, 0);
        g.setStatus(GameStatus.WAITING);
        assertEquals(300_000, g.remainingMs(Side.WHITE, NOW));
    }

    @Test
    void timedOutSideShowsZeroAfterFinish() {
        Game g = game(600, 0);
        g.setStatus(GameStatus.FINISHED);
        g.setEndReason(GameEndReason.TIMEOUT);
        assertEquals(0, g.remainingMs(Side.WHITE, NOW));
        assertEquals(600_000, g.remainingMs(Side.BLACK, NOW));
    }
}