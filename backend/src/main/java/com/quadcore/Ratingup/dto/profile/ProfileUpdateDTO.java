package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.model.profile.User;
import jakarta.validation.constraints.Email;

public record ProfileUpdateDTO(
        Long id,
        String name,
        String nickname,
        @Email(message = "Formato de email inválido!")
        String email,
        String telefone,
        Roles role
) {
        public ProfileUpdateDTO(User user) {
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
