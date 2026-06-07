package com.quadcore.Ratingup.dto.board;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SubtopicPracticeSessionRequestDTO(

        @NotNull(message = "o id da sessão é necessário")
        Long sessionId,

        @NotNull(message = "Indicar a peça é obrigatório")
        Piece piece,

        @NotNull(message = "Posição inical da peça deve ser indicada")
        Square posInitial,

        @NotNull(message = "Posição final da peça deve ser indicada")
        Square posFinal

) {}
