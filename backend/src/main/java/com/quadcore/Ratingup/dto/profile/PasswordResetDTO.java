package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetDTO(
        @NotBlank String token,
        @NotBlank String newPassword
) {}
