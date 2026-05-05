package com.quadcore.Ratingup.dto.progresso;

import com.quadcore.Ratingup.model.profile.Progress;
import jakarta.validation.constraints.NotNull;

public record ProgressUpdateDTO(
        @NotNull Integer chapter,
        @NotNull Integer subtopic
) {
    public ProgressUpdateDTO(Progress progresso) {
        this(
                progresso.getChapters(),
                progresso.getSubtopics()
        );
    }
}
