package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.enums.TimeControlCategory;

public record GameSummaryDTO(
        Long gameId,
        GameStatus status,
        String myColor,
        String opponentNickname,
        int initialTimeSeconds,
        int incrementSeconds,
        TimeControlCategory timeControlCategory
) {}