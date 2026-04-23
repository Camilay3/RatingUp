package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.model.profile.User;

public record ProfileResponseDTO(
        Long id,
        String nome,
        String nickname,
        String email,
        String telefone,
        String role,
        Integer capitulo,
        Integer subtopico
) {
    public ProfileResponseDTO(User user) {
        this(
                user.getId(),
                user.getNome(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole(),
                user.getProgresso().getCapitulo(),
                user.getProgresso().getSubtopico()
                );
    }
}
