package com.quadcore.Ratingup.dto.book;

import jakarta.validation.constraints.NotNull;

public record SubtopicIdRequestDto(
        @NotNull(message = "subtopicId é obrigatório")
        Long subtopicId
) {
}
