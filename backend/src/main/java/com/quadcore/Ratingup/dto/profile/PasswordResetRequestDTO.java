package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDTO(
        @NotBlank(message = "Email é obigatório")
        @Email(message = "Formato de email inválido")
        String email
) {}
