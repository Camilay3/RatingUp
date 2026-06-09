package com.quadcore.Ratingup.dto.board;

public record SubtopicPracticeSessionResponseDTO(

        Long sessionId,
        String fen,
        String status,
        String initialFen
) {}