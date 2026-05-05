package com.quadcore.Ratingup.dto.book;

import com.quadcore.Ratingup.model.book.Chapters;

public record SubtopicRequestDTO(
        String title,
        Integer displayOrder,
        Chapters chapterId
) {}
