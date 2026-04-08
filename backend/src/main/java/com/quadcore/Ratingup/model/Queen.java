package com.quadcore.Ratingup.model;

public class Queen extends Piece {

    int start = Integer.parseInt(posStart);
    int fim = Integer.parseInt(posFinal);

    @Override
    public boolean isValidMove(int newX, int newY) {

        int dx = Math.abs(newX - start);
        int dy = Math.abs(newY - fim);

        if (start == newX) {
            return true;
        }

        if (fim == newY) {
            return true;
        }

        if (dx == dy) {
            return true;
        }

        return false;
    }
}