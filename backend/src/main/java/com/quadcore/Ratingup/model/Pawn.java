package com.quadcore.Ratingup.model;

public class Pawn extends Piece {

    int start = Integer.parseInt(posStart);
    int fim = Integer.parseInt(posFinal);

    @Override
    public boolean isValidMove(int newX, int newY) {
        int dx = Math.abs(newX - start);
        int dy = Math.abs(newY - fim);

        if (moves == 0) {
                
        }

        return false;
    }
}
