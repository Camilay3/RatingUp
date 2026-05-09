package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.book.SubtopicRequestDTO;
import com.quadcore.Ratingup.dto.book.SubtopicResponseDTO;
import com.quadcore.Ratingup.model.book.Subtopics;

public abstract class SubtopicsMapper {
    public static SubtopicResponseDTO toResponseDTO(Subtopics subtopic) {
        return new SubtopicResponseDTO(subtopic);
    }

    public static SubtopicRequestDTO toRequestDTO(Subtopics subtopic) {
        return new SubtopicRequestDTO(subtopic);
    }
}
