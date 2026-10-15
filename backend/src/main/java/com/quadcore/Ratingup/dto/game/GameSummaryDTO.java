package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.GameStatus;

public record GameSummaryDTO(
        Long gameId,
        GameStatus status,
        String myColor,
        String opponentNickname
) {}