package com.quadcore.Ratingup.dto.book;

import com.quadcore.Ratingup.model.book.Subtopics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubtopicRequestDTO(
        @NotNull(message = "id não pode ser vazaio")
        Long chapterId,

        @NotBlank(message = "titulo não pode ser vazio")
        String title,

        @NotNull (message = "ordem não pode ser vazia")
        Integer displayOrder
) {
    public SubtopicRequestDTO(Subtopics subtopic) {
        this(
                subtopic.getChapter().getId(),
                subtopic.getTitle(),
                subtopic.getDisplayOrder()
        );
    }
}
