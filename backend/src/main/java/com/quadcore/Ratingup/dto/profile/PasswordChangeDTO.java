package com.quadcore.Ratingup.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeDTO(

        @NotBlank(message = "Senha antiga não pode ser vazia")
        String oldPassword,

        @NotBlank(message = "Senha não pode ser vazia")

        @Size(
                min = 8, max = 12,
                message = "A senha deve ter entre 8 e 12 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).*$",
                message = "A senha deve conter letra maiúscula, minúscula, número e símbolo"
        )
        @Schema(example = "SenhaExemplo@123")
        String newPassword
) {}
