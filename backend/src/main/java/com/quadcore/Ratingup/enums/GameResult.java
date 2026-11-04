package com.quadcore.Ratingup.enums;

import com.github.bhlangonijr.chesslib.Side;

public enum GameResult {
    WHITE_WINS,
    BLACK_WINS,
    DRAW;

    public double scoreFor(Side side) {
        return switch (this) {
            case DRAW -> 0.5;
            case WHITE_WINS -> side == Side.WHITE ? 1.0 : 0.0;
            case BLACK_WINS -> side == Side.BLACK ? 1.0 : 0.0;
        };
    }
}