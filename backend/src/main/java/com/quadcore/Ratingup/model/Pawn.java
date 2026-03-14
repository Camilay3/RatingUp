package com.quadcore.Ratingup.model;

public class Pawn extends Piece {

    private int moves = 0;

    @Override
    public boolean isValidMove(int newX, int newY) {
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        if (moves == 0) {
                
        }

        return false;
    }
}
