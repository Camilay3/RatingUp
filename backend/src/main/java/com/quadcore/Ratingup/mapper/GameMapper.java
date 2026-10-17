package com.quadcore.Ratingup.mapper;

import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.game.GameEventDTO;
import com.quadcore.Ratingup.dto.game.GameStateDTO;
import com.quadcore.Ratingup.dto.game.GameSummaryDTO;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.model.game.Game;

public class GameMapper {

    public static GameEventDTO toEvent(Game game, Move move, Side turn, BoardStatus boardStatus) {
        return new GameEventDTO(
                "MOVE",
                game.getId(),
                game.getMoveNumber(),
                move.getFrom().toString().toLowerCase(),
                move.getTo().toString().toLowerCase(),
                game.getCurrentFen(),
                turn.name(),
                boardStatus,
                game.getStatus());
    }

    public static GameEventDTO toStartedEvent(Game game) {
        return new GameEventDTO(
                "STARTED",
                game.getId(),
                game.getMoveNumber(),
                null,
                null,
                game.getCurrentFen(),
                "WHITE",
                BoardStatus.NORMAL,
                game.getStatus());
    }

    public static GameStateDTO toState(Game game, Side turn, Side myColor,
                                       BoardStatus boardStatus, String opponentNickname) {
        return new GameStateDTO(
                game.getId(),
                game.getCurrentFen(),
                game.getMoveNumber(),
                turn.name(),
                myColor.name(),
                game.getStatus(),
                boardStatus,
                opponentNickname);
    }

    public static GameSummaryDTO toSummary(Game game, Side myColor, String opponentNickname) {
        return new GameSummaryDTO(
                game.getId(),
                game.getStatus(),
                myColor == null ? null : myColor.name(),
                opponentNickname);
    }
}