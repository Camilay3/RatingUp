package com.quadcore.Ratingup.dto.progresso;

import jakarta.validation.constraints.NotNull;

public record ProgressUpdateDTO(
        @NotNull Integer chapter,
        @NotNull Integer subtopic
) {}
