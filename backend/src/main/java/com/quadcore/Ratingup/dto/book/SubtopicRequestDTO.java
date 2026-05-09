package com.quadcore.Ratingup.dto.book;

import com.quadcore.Ratingup.model.book.Subtopics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubtopicRequestDTO(
        @NotNull Long chapterId,
        @NotBlank String title,
        @NotNull Integer displayOrder
) {
    public SubtopicRequestDTO(Subtopics subtopic) {
        this(
                subtopic.getChapter().getId(),
                subtopic.getTitle(),
                subtopic.getDisplayOrder()
        );
    }
}
