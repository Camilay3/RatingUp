package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.model.profile.User;

public record ProfileResponseDTO(
        Long id,
        String name,
        String nickname,
        String email,
        String telefone,
        Roles role
) {
    public ProfileResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole()
        );
    }
}
