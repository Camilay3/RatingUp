package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressResponseDTO;
import com.quadcore.Ratingup.model.profile.Progress;

public abstract class ProgressoMapper {

    public static ProgressUpdateDTO toUpdate(Progress progresso) {
        return new ProgressUpdateDTO(progresso);
    }

    public static ProgressResponseDTO toResponse(Progress progresso) {
        return new ProgressResponseDTO(progresso);
    }
}
