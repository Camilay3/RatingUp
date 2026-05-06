package com.quadcore.Ratingup.dto.progresso;
import com.quadcore.Ratingup.model.profile.Progress;

public record ProgressResponseDTO(
        Long userId,
        Integer chapter,
        Integer subtopic
) {
    public ProgressResponseDTO(Progress progresso) {
        this(
                progresso.getUser().getId(),
                progresso.getChapters(),
                progresso.getSubtopics()
        );
    }
}
