package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.MoveRequestDTO;
import com.quadcore.Ratingup.util.BoardPosition;
import org.springframework.stereotype.Service;

@Service
public class MoveValidationService {

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
//
//
//
//
//
//
//
//    public boolean isValidQueenMove(int newX, int newY) {
//
//        return false;
//    }

}