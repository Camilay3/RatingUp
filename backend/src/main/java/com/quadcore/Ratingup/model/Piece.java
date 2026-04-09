package com.quadcore.Ratingup.model;

public abstract class Piece {
    protected String posStart;
    protected String posFinal;
    protected int moves;

    public abstract boolean isValidMove(int newX, int newY);
}
