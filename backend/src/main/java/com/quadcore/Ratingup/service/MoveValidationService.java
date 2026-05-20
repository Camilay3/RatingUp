package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.quadcore.Ratingup.dto.board.MoveRequestDTO;
import com.quadcore.Ratingup.dto.board.MoveResponseDTO;
import com.quadcore.Ratingup.enums.BoardStatus;
import com.quadcore.Ratingup.enums.Roles;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveValidationService {

    public Boolean validateMovement(MoveRequestDTO moveDto){
        Board board = new Board();
        board.loadFromFen(moveDto.fen());
        Move move = new Move(moveDto.posInitial(),moveDto.posFinal(),moveDto.piece());
        List<Move> moveList = board.legalMoves();
        Boolean isPossible = moveList.contains(move);
        return isPossible;
    }

    public MoveResponseDTO performMovement(MoveRequestDTO moveDto) {
        Board board = new Board();
        board.loadFromFen(moveDto.fen());
        Move move = new Move(moveDto.posInitial(), moveDto.posFinal(), moveDto.piece());

        List<Move> moveList = board.legalMoves();
        if (!moveList.contains(move)) {
            throw new IllegalArgumentException("Movimento inválido!");
        }

        board.doMove(move);

        return new MoveResponseDTO(board.getFen(), checkStatus(board));
    }

    private String checkStatus(Board board) {
        if (board.isMated()) {
            return BoardStatus.CHECKMATE.name();
        } else if (board.isDraw()) {
            return BoardStatus.DRAW.name();
        } else if (board.isKingAttacked()) {
            return BoardStatus.CHECK.name();
        } else {
            return BoardStatus.NORMAL.name();
        }
    }

}