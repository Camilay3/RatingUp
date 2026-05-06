package com.quadcore.Ratingup.dto.board;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MoveRequestDTO(
        @NotNull
        @Pattern(regexp = "^(P|N|B|R|Q|K|p|n|b|r|q|k)")//maisuculas brancas,minusculas pretas
        Piece piece,
        @NotNull
        @Pattern(regexp = "[A-H][1-8]")
        Square posInitial,
        @NotNull
        @Pattern(regexp = "[A-H][1-8]")
        Square posFinal,
        @NotBlank
        String fen //como ta o tebuleiro

) {}
