package com.quadcore.Ratingup.dto.progresso;

import com.quadcore.Ratingup.model.profile.Progress;
import jakarta.validation.constraints.NotNull;

public record ProgressUpdateDTO(
        @NotNull(message = "capítulo é obrigatório")
        Integer chapter,

        @NotNull(message = "subtópico é obrigatório")
        Integer subtopic
) {
    public ProgressUpdateDTO(Progress progresso) {
        this(
                progresso.getChapters(),
                progresso.getSubtopics()
        );
    }
}
