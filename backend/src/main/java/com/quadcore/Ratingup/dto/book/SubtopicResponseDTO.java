package com.quadcore.Ratingup.dto.book;

import com.quadcore.Ratingup.model.book.Subtopics;

public record SubtopicResponseDTO(
        Long id,
        Long chapterId,
        String title,
        Integer displayOrder,
        String content,
        String practiceExplanation
        ) {
    public SubtopicResponseDTO(Subtopics subtopic) {
        this(
                subtopic.getId(),
                subtopic.getChapter().getId(),
                subtopic.getTitle(),
                subtopic.getDisplayOrder(),
                subtopic.getContent(),
                subtopic.getPracticeExplanation()
        );
    }
}
