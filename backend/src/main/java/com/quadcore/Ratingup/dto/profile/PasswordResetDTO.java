package com.quadcore.Ratingup.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetDTO(

        @NotBlank
        String token,

        @NotBlank(message = "Nova senha inválida")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$",
                message = "A senha precisa ter entre 8 a 12 caracteres, com maiúsculas, minúsculas, números e símbolos")
        @Schema(example = "SenhaExemplo@123")
        String newPassword
) {}
