package com.quadcore.Ratingup.util;

public record BoardPosition(int col, int row) {

    public static BoardPosition from(String pos) {
        int col = pos.charAt(0) - 'a';
        int row = pos.charAt(1) - '1';
        return new BoardPosition(col, row);
    }

    public int toIndex() {
        return col * 8 +row;
    }
}
