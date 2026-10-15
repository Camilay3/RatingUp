package com.quadcore.Ratingup.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GameMoveResquestDTO(
        @NotBlank @Pattern(regexp = "[a-h][1-8]", message = "Casa de origem inválida") String from,
        @NotBlank @Pattern(regexp = "[a-h][1-8]", message = "Casa de destino inválida") String to,
        @Pattern(regexp = "[qrbn]", message = "Peça de promoção inválida") String promotion
) {
}