package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.progresso.AtualizaProgressoDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressoResponseDTO;
import com.quadcore.Ratingup.model.profile.Progresso;

public abstract class ProgressoMapper {

    public static AtualizaProgressoDTO toUpdate(Progresso progresso) {
        AtualizaProgressoDTO dto = new AtualizaProgressoDTO(progresso);
        return dto;
    }

    public static ProgressoResponseDTO toResponse(Progresso progresso) {
        ProgressoResponseDTO dto = new ProgressoResponseDTO(progresso);
        return dto;
    }
}
