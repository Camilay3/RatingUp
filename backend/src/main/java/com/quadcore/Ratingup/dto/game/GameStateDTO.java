package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.*;

public record GameStateDTO(
        Long gameId,
        String fen,
        int moveNumber,
        String turn,
        String myColor,
        GameStatus gameStatus,
        BoardStatus boardStatus,
        String opponentNickname,
        int initialTimeSeconds,
        int incrementSeconds,
        TimeControlCategory timeControlCategory,
        GameResult result,
        GameEndReason endReason,
        Integer myRatingChange,
        Integer myRating,
        Integer opponentRating,
        long whiteTimeMs,
        long blackTimeMs
) {}