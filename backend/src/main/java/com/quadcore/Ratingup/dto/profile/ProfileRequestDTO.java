package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.model.profile.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfileRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Nickname é obrigatório") String nickname,
        @NotBlank(message = "Email é obrigatório") @Email(message = "Formato de email inválido") String email,
        @NotBlank(message = "Telefone é obrigatório") String telefone,
        @NotBlank(message = "Senha é obrigatória") @Size(min = 8, max = 12, message = "A senha deve conter entre 8 a 12 caracteres") String password
) {
    public ProfileRequestDTO(User user) {
        this(
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getPassword()
        );
    }
}
