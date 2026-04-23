package com.quadcore.Ratingup.dto.progresso;
import com.quadcore.Ratingup.model.Progresso;
import com.quadcore.Ratingup.model.User;

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
