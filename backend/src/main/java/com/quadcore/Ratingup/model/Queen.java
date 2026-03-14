package com.quadcore.Ratingup.model;

import com.quadcore.Ratingup.model.Piece;

public class Queen extends Piece {

    @Override
    public boolean isValidMove(int newX, int newY) {

        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        if (x == newX) {
            return true;
        }

        if (y == newY) {
            return true;
        }

        if (dx == dy) {
            return true;
        }

        return false;
    }
}