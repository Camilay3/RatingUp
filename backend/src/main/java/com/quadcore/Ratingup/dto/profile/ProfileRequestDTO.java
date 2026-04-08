package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequestDTO(
        @NotBlank String nome,
        @NotBlank String nickname,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotBlank @Size(min = 8, max = 12) String senha
) {
    public ProfileRequestDTO(User user) {
        this(
                user.getNome(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getSenha()
        );
    }
}
