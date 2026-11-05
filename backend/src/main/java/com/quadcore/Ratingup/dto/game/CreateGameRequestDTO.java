package com.quadcore.Ratingup.dto.game;

import com.quadcore.Ratingup.dto.game.TimeControlDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CreateGameRequestDTO(
        @Min(value = 15, message = "Tempo inicial mínimo é 15 segundos")
        @Max(value = 10800, message = "Tempo inicial máximo é 3 horas") Integer initialTimeSeconds,
        @Min(value = 0, message = "Incremento não pode ser negativo")
        @Max(value = 180, message = "Incremento máximo é 180 segundos") Integer incrementSeconds
) {
    public TimeControlDTO toTimeControl() {
        return new TimeControlDTO(
                initialTimeSeconds == null ? TimeControlDTO.DEFAULT.initialSeconds() : initialTimeSeconds,
                incrementSeconds == null ? TimeControlDTO.DEFAULT.incrementSeconds() : incrementSeconds);
    }
}