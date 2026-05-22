package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.model.profile.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequestDTO(

        @Size(min = 10, max = 100, message = "O Nome deve ter entre 10 e 100 caracteres")
        String name,

        @Size(min = 8, max = 16, message = "O nickname deve ter entre 8 a 16 caracteres")
        String nickname,

        @Email(message = "Formato de email inválido!")
        String email,

        @Pattern(regexp = "\\d{8,11}", message = "Telefone deve conter apenas números, entre 8 e 11 dígitos")
        String telefone
) {
    public ProfileUpdateRequestDTO(User user) {
        this(
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone()
        );
    }
}
