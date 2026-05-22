package com.quadcore.Ratingup.dto.book;

import com.quadcore.Ratingup.model.book.Subtopics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubtopicRequestDTO(
        @NotNull(message = "Id não pode ser vazaio")
        Long chapterId,

        @NotBlank(message = "Título não pode ser vazio")
        String title,

        @NotNull (message = "Ordem não pode ser vazia")
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
