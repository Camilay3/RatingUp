package com.quadcore.Ratingup.controller;
import com.quadcore.Ratingup.dto.progresso.AtualizaProgressoDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressoResponseDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.mapper.ProgressoMapper;
import com.quadcore.Ratingup.model.profile.Progresso;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.ProgressoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progresso")
public class ProgressoController {

    private final ProgressoService progressoService;

    public ProgressoController(ProgressoService progressoService) {
        this.progressoService = progressoService;
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<ApiResponse<?>> allowedPhases(@AuthenticationPrincipal User logado) {
        Progresso progresso = progressoService.allowedPhases(logado.getEmail());
        ProgressoResponseDTO dto = new ProgressoResponseDTO(
                progresso.getUser().getId(),
                progresso.getCapitulo(),
                progresso.getSubtopico());
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso encontrado", dto));
    }

    @PostMapping("/atualiza-fase")
    public ResponseEntity<ApiResponse<?>> updateCurrentPhase(@AuthenticationPrincipal User logado, @Valid @RequestBody AtualizaProgressoDTO dto) {
        Progresso atualizado = progressoService.updateCurrentPhase(logado.getEmail(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso atualizado", ProgressoMapper.toResponse(atualizado)));
    }
}
