package com.quadcore.Ratingup.dto.progresso;

import com.quadcore.Ratingup.model.profile.Progresso;
import jakarta.validation.constraints.NotNull;

public record AtualizaProgressoDTO(
        @NotNull Integer capitulo,
        @NotNull Integer subtopico
) {
    public AtualizaProgressoDTO(Progresso progresso) {
        this(
                progresso.getCapitulo(),
                progresso.getSubtopico()
        );
    }
}
