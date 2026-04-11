package com.quadcore.Ratingup.dto.response;

public record ApiResponse<T>(
        boolean sucesso,
        String mensagem,
        T dados
) {}
