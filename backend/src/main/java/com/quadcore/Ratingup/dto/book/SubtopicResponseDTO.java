package com.quadcore.Ratingup.dto.book;

import com.quadcore.Ratingup.model.book.Subtopics;

public record SubtopicResponseDTO(
        Long id,
        Long chapterId,
        String title,
        Integer displayOrder
) {
    public SubtopicResponseDTO(Subtopics subtopic) {
        this(
                subtopic.getId(),
                subtopic.getChapter().getId(),
                subtopic.getTitle(),
                subtopic.getDisplayOrder()
        );
    }
}
