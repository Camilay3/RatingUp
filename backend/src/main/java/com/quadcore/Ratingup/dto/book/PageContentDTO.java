package com.quadcore.Ratingup.dto.book;

public record PageContentDTO(
        String type,
        Long id,
        Long chapterId,
        String title,
        Integer displayOrder,
        String subtopicImageUrl
) {}
