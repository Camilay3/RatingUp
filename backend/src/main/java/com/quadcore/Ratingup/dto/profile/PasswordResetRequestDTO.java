package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDTO(

        @NotBlank(message = "email é obigatório")
        @Email(message = "formato de email inválido")
        String email

) {}
