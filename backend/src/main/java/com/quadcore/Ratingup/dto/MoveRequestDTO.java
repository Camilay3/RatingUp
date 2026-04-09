package com.quadcore.Ratingup.dto;

public record MoveRequestDTO(
        String piece,
        String posStart,
        String posFinal,
        int moves
) {
}
