package com.quadcore.Ratingup.dto.progresso;
import com.quadcore.Ratingup.model.profile.Progresso;

public record ProgressoResponseDTO(
        Long userId,
        Integer capitulo,
        Integer subtopico
) {
    public ProgressoResponseDTO(Progresso progresso) {
        this(
                progresso.getUser().getId(),
                progresso.getCapitulo(),
                progresso.getSubtopico()
        );
    }
}
