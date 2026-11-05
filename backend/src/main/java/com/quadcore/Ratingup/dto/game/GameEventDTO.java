package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.enums.GameEndReason;
import com.quadcore.Ratingup.enums.GameResult;
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
        GameStatus gameStatus,
        GameResult result,
        GameEndReason endReason,
        Integer whiteRatingChange,
        Integer blackRatingChange,
        long whiteTimeMs,
        long blackTimeMs
) {
}