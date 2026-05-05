package com.quadcore.Ratingup.dto.profile;

public record PasswordChangeDTO(
        String oldPassword,
        String newPassword
) {}
