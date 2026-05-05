package com.quadcore.Ratingup.controller;
import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressResponseDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.mapper.ProgressoMapper;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Progresso", description = "Endpoints para gerenciamento do progresso do usuário")
@RestController
@RequestMapping("/progresso")
public class ProgressController {

    private final ProgressService progressoService;

    public ProgressController(ProgressService progressoService) {
        this.progressoService = progressoService;
    }

    @Operation(summary = "Retorna a fase atual do usuário")
    @GetMapping("/disponiveis")
    public ResponseEntity<ApiResponse<?>> allowedPhases(@AuthenticationPrincipal User logado) {
        Progress progresso = progressoService.allowedPhases(logado.getEmail());
        ProgressResponseDTO dto = new ProgressResponseDTO(
                progresso.getUser().getId(),
                progresso.getChapters(),
                progresso.getSubtopics());
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso encontrado", dto));
    }

    @Operation(summary = "Atualiza a fase atual do usuário")
    @PostMapping("/atualiza-fase")
    public ResponseEntity<ApiResponse<?>> updateCurrentPhase(@AuthenticationPrincipal User logado, @Valid @RequestBody ProgressUpdateDTO dto) {
        Progress atualizado = progressoService.updateCurrentPhase(logado.getEmail(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso atualizado", ProgressoMapper.toResponse(atualizado)));
    }
}
