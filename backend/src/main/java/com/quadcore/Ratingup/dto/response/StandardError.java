package com.quadcore.Ratingup.dto.response;

import java.time.LocalDateTime;

public record StandardError(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String code,
        String message,
        Object errors
) {}
