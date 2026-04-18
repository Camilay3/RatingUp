package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.board.MoveRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveValidationService {

    public Boolean validarMovimento(MoveRequestDTO movimento){
        Board board = new Board();
        board.loadFromFen(movimento.fen());
        Move move = new Move(movimento.posInicial(),movimento.posFinal(),movimento.piece());
        List<Move> moveList = board.legalMoves();
        return moveList.contains(move);
    }

    public String executarMovimento(MoveRequestDTO movimento) {
        Board board = new Board();
        board.loadFromFen(movimento.fen());
        Move move = new Move(movimento.posInicial(), movimento.posFinal(), movimento.piece());
        List<Move> moveList = board.legalMoves();
        if (!moveList.contains(move)) {
            throw new IllegalArgumentException("Movimento inválido!");
        }
        board.doMove(move);
        return board.getFen();
    }


//    public boolean validateMove(MoveRequestDTO move) {
//
//        // esse switch case diferenciado é o switch expression, ele usa lambda msm
//        return switch (move.piece().toUpperCase()) {
//            case "QUEEN" -> isValidQueenMove(start, end);
//            case "PAWN" -> isValidPawnMove(start, end);
//            default -> false;
//        };
//    }
//
//
//    public boolean isValidPawnMove(BoardPosition start, BoardPosition end) {
//        int colDiff = Math.abs(end.col() - start.col());
//        int rowDiff = end.row() - start.row();
//    }
//
//    public boolean isValidQueenMove(int newX, int newY) {
//
//        return false;
//    }

}