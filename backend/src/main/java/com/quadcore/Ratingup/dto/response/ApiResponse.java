package com.quadcore.Ratingup.dto.response;

public record ApiResponse<T>(
        boolean status,
        String message,
        T data
) {}
