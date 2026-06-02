package com.quadcore.Ratingup.dto.board;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SubtopicPracticeSessionRequestDTO(

        @NotNull(message = "Indicar a peça é obrigatório")
        @Pattern(regexp = "^(P|N|B|R|Q|K|p|n|b|r|q|k)",
                message = "Peça deve ser indicada como um dos seguintes caracteres:P|N|B|R|Q|K|p|n|b|r|q|k " +
                        "maiúsculas para brancas,minúsculas para pretas")
        Piece piece,

        @NotNull(message = "Posição inical da peça deve ser indicada")
        @Pattern(regexp = "[A-H][1-8]", message = "O padrão utilizada para indicar posição é uma letra maiúscula entre[A-H] " +
                "juntamente com um dígito entre [1-8]")
        Square posInitial,

        @NotNull(message = "Posição final da peça deve ser indicada")
        @Pattern(regexp = "[A-H][1-8]", message = "O padrão utilizada para indicar posição é uma letra maiúscula entre[A-H] " +
                "juntamente com um dígito entre [1-8]")
        Square posFinal

        //@NotBlank //fazendo por idsession agr
        //String fen //como ta o estado atual do tabuleiro(mateus) //melhorar a validação do fen(mateus)

) {}
