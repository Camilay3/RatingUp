package com.quadcore.Ratingup.model;

public abstract class Piece {
    protected int x;
    protected int y;

    public abstract boolean isValidMove(int newX, int newY);
}
