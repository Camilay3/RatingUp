package com.quadcore.Ratingup.dto.board;

import java.util.List;

public record QuizResponseDTO(
        Long subtopicId,
        String questionText,
        List<QuizOptionDTO> options
) {}
