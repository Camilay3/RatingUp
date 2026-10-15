package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.enums.GameStatus;

public record GameEventDTO(
        String type,
        Long gameId,
        int moveNumber,
        String from,
        String to,
        String fen,
        String turn,
        BoardStatus boardStatus,
        GameStatus gameStatus
) {
}
