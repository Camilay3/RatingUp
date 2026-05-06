package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.board.MoveRequestDTO;
import com.quadcore.Ratingup.dto.board.MoveResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveValidationService {

    public Boolean validateMovement(MoveRequestDTO movimento){
        Board board = new Board();
        board.loadFromFen(movimento.fen());
        Move move = new Move(movimento.posInitial(),movimento.posFinal(),movimento.piece());
        List<Move> moveList = board.legalMoves();
        return moveList.contains(move);
    }

    public MoveResponseDTO performMovement(MoveRequestDTO movimento) {
        Board board = new Board();
        board.loadFromFen(movimento.fen());
        Move move = new Move(movimento.posInitial(), movimento.posFinal(), movimento.piece());

        List<Move> moveList = board.legalMoves();
        if (!moveList.contains(move)) {
            throw new IllegalArgumentException("Movimento inválido!");
        }

        board.doMove(move);

        return new MoveResponseDTO(board.getFen(), checkStatus(board));
    }

    private String checkStatus(Board board) {
        if (board.isMated()) {
            return "CHECKMATE";
        } else if (board.isDraw()) {
            return "DRAW";
        } else if (board.isKingAttacked()) {
            return "CHECK";
        } else {
            return "NORMAL";
        }
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