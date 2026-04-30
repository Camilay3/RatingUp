package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDTO(@NotBlank String email) {
}
