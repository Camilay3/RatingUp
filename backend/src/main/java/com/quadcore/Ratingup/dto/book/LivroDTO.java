package com.quadcore.Ratingup.dto.book;

import java.util.List;

public record LivroDTO(
        List<PaginaDTO> paginas,
        int totalPaginas
) {}
