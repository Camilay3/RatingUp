package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.enums.GameStatus;

public record GameStateDTO(
        Long gameId,
        String fen,
        int moveNumber,
        String turn,
        String myColor,
        GameStatus gameStatus,
        BoardStatus boardStatus,
        String opponentNickname
) {}
