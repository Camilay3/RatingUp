package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.MoveRequestDTO;
import com.quadcore.Ratingup.service.MoveValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/move")
@RestController
public class MoveController {

    @Autowired
    MoveValidationService moveValidationService;

    @PostMapping
    public ResponseEntity<Boolean> validarMovimento(@RequestBody @Valid MoveRequestDTO movimentoDto){
        Boolean isPossible = moveValidationService.validarMovimento(movimentoDto);
        return ResponseEntity.ok(isPossible);
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executarMovimento(@RequestBody @Valid MoveRequestDTO movimentoDto) {
        String novoFen = moveValidationService.executarMovimento(movimentoDto);
        return ResponseEntity.ok(novoFen);
    }
}
