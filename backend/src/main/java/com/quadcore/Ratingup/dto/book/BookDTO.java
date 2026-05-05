package com.quadcore.Ratingup.dto.book;

import java.util.List;

public record BookDTO(
        List<PageDTO> pages,
        int totalPages
) {}
