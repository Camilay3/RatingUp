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

    @Operation(summary = "Retorna a fase atual do usuário",description = "devolve um dto com id do usuário,juntamente com último capítulo e último subtópico desbloqueado")
    @GetMapping("/disponiveis")
    public ResponseEntity<ApiResponse<?>> allowedPhases(@AuthenticationPrincipal User loggedUser) {
        Progress progress = progressoService.allowedPhases(loggedUser.getEmail());
        ProgressResponseDTO dto = new ProgressResponseDTO(
                progress.getUser().getId(),
                progress.getChapters(),
                progress.getSubtopics());
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso do usuário encontrado", dto));
    }

    @Operation(summary = "Atualiza a fase atual do usuário",description = "altera campos 'chapter' e 'subtopic' do progresso do usuário")
    @PostMapping("/atualiza-fase")
    public ResponseEntity<ApiResponse<?>> updateCurrentPhase(@AuthenticationPrincipal User logado, @Valid @RequestBody ProgressUpdateDTO dto) {
        Progress updated = progressoService.updateCurrentPhase(logado.getEmail(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso atualizado", ProgressoMapper.toResponse(updated)));
    }
}
