package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateDTO(

        Long id,

        @NotBlank(message = "O nome completo não pode ser vazio!")
        String nome,

        @NotBlank(message = "O nickname não pode ser vazio!")
        String nickname,

        @NotBlank(message = "Email não pode ser vazio!")
        @Email(message = "Formato de email inválido!")
        String email,

        @NotBlank(message = "Telefone não pode ser vazio!")
        String telefone
) {
}
