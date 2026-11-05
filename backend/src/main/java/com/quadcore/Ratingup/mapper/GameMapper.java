package com.quadcore.Ratingup.mapper;

import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.game.*;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.model.game.PlayerRating;
import com.quadcore.Ratingup.enums.SkillLevel;
import com.quadcore.Ratingup.enums.TimeControlCategory;
import java.time.LocalDateTime;

public class GameMapper {

    public static GameEventDTO toEvent(Game game, Move move, Side turn, BoardStatus boardStatus) {
        return build("MOVE", game,
                move.getFrom().toString().toLowerCase(),
                move.getTo().toString().toLowerCase(),
                turn, boardStatus);
    }

    public static GameEventDTO toStartedEvent(Game game) {
        return build("STARTED", game, null, null, Side.WHITE, BoardStatus.NORMAL);
    }

    public static GameEventDTO toResignedEvent(Game game, Side turn, BoardStatus boardStatus) {
        return build("RESIGNED", game, null, null, turn, boardStatus);
    }

    public static GameEventDTO toTimeoutEvent(Game game, Side turn, BoardStatus boardStatus) {
        return build("TIMEOUT", game, null, null, turn, boardStatus);
    }

    private static GameEventDTO build(String type, Game game, String from, String to,
                                      Side turn, BoardStatus boardStatus) {
        LocalDateTime now = LocalDateTime.now();
        return new GameEventDTO(
                type,
                game.getId(),
                game.getMoveNumber(),
                from,
                to,
                game.getCurrentFen(),
                turn.name(),
                boardStatus,
                game.getStatus(),
                game.getResult(),
                game.getEndReason(),
                game.getWhiteRatingChange(),
                game.getBlackRatingChange(),
                game.remainingMs(Side.WHITE, now),
                game.remainingMs(Side.BLACK, now));
    }

    public static GameStateDTO toState(Game game, Side turn, Side myColor,
                                       BoardStatus boardStatus, String opponentNickname,
                                       Integer myRating, Integer opponentRating) {
        Integer myChange = myColor == Side.WHITE ? game.getWhiteRatingChange() : game.getBlackRatingChange();
        LocalDateTime now = LocalDateTime.now();
        return new GameStateDTO(
                game.getId(), game.getCurrentFen(), game.getMoveNumber(), turn.name(), myColor.name(),
                game.getStatus(), boardStatus, opponentNickname,
                game.getInitialTimeSeconds(), game.getIncrementSeconds(), game.getTimeControlCategory(),
                game.getResult(), game.getEndReason(), myChange,
                myRating, opponentRating,
                game.remainingMs(Side.WHITE, now), game.remainingMs(Side.BLACK, now));
    }

    public static GameSummaryDTO toSummary(Game game, Side myColor, String opponentNickname) {
        return new GameSummaryDTO(
                game.getId(),
                game.getStatus(),
                myColor == null ? null : myColor.name(),
                opponentNickname,
                game.getInitialTimeSeconds(),
                game.getIncrementSeconds(),
                game.getTimeControlCategory());
    }

    public static PlayerRatingDTO toRatingDTO(PlayerRating p) {
        return new PlayerRatingDTO(p.getCategory(), p.getRating(), p.getGamesPlayed(),
                p.getWins(), p.getLosses(), p.getDraws(), p.getPerformanceRating(), p.getSkillLevel());
    }

    /** Categoria que o jogador nunca jogou: rating inicial, sem partidas e sem classificação. */
    public static PlayerRatingDTO toDefaultRatingDTO(TimeControlCategory category) {
        return new PlayerRatingDTO(category, PlayerRating.DEFAULT_RATING, 0, 0, 0, 0, null, SkillLevel.UNRATED);
    }
}