package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.MoveRequestDTO;
import com.quadcore.Ratingup.dto.board.MoveResponseDTO;
import com.quadcore.Ratingup.service.MoveValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Movimentos", description = "Endpoints para gerenciamento dos movimentos das peças")
@RequestMapping("/move")
@RestController
public class MoveController {

    @Autowired
    MoveValidationService moveValidationService;

    @Operation(summary = "Valida o movimento da peça")
    @GetMapping
    public ResponseEntity<Boolean> validateMovement(@RequestBody @Valid MoveRequestDTO movementDto){
        return ResponseEntity.ok(moveValidationService.validateMovement(movementDto));
    }

    @Operation(summary = "Executa o movimento da peça",description = "valida o movimento,faz o movimento e retorna o estado atual do tabuleiro juntamente do status da partida")
    @PostMapping("/execute")
    public ResponseEntity<MoveResponseDTO> performMovement(@RequestBody @Valid MoveRequestDTO movementDto) {
        return ResponseEntity.ok(moveValidationService.performMovement(movementDto));
    }
}
