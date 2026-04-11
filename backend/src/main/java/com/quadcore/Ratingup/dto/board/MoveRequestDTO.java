package com.quadcore.Ratingup.dto.board;

public record MoveRequestDTO(
        String piece,
        String posStart,
        String posFinal,
        int moves
) {
}
