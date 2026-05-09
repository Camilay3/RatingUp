package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.MoveRequestDTO;
import com.quadcore.Ratingup.dto.board.MoveResponseDTO;
import com.quadcore.Ratingup.service.MoveValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Movimentos", description = "Endpoints para gerenciamento dos movimentos das peças")
@RequestMapping("/move")
@RestController
public class MoveController {

    @Autowired
    MoveValidationService moveValidationService;

    @Operation(summary = "Valida o movimento da peça")
    @PostMapping
    public ResponseEntity<Boolean> validateMovement(@RequestBody @Valid MoveRequestDTO movimentoDto){
        Boolean isPossible = moveValidationService.validateMovement(movimentoDto);
        return ResponseEntity.ok(isPossible);
    }

    @Operation(summary = "Executa o movimento da peça")
    @PostMapping("/execute")
    public ResponseEntity<MoveResponseDTO> performMoviment(@RequestBody @Valid MoveRequestDTO movimentoDto) {
        MoveResponseDTO response = moveValidationService.performMovement(movimentoDto);
        return ResponseEntity.ok(response);
    }
}
