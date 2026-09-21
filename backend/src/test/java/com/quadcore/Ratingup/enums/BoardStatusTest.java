package com.quadcore.Ratingup.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardStatusTest {

    @Test
    @DisplayName("Should contain expected values")
    void testEnumValues() {
        BoardStatus[] statuses = BoardStatus.values();
        assertEquals(6, statuses.length);
        assertEquals(BoardStatus.NORMAL, BoardStatus.valueOf("NORMAL"));
        assertEquals(BoardStatus.CHECK, BoardStatus.valueOf("CHECK"));
        assertEquals(BoardStatus.CHECKMATE, BoardStatus.valueOf("CHECKMATE"));
        assertEquals(BoardStatus.DRAW, BoardStatus.valueOf("DRAW"));
        assertEquals(BoardStatus.WRONG_MOVE, BoardStatus.valueOf("WRONG_MOVE"));
        assertEquals(BoardStatus.COMPLETED, BoardStatus.valueOf("COMPLETED"));
    }
}
