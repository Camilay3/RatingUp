package com.quadcore.Ratingup.controller;
import com.quadcore.Ratingup.dto.progresso.ProgressoResponseDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.model.Progresso;
import com.quadcore.Ratingup.service.ProgressoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capitulo")
public class ProgressoController {

    private final ProgressoService progressoService;

    public ProgressoController(ProgressoService progressoService) {
        this.progressoService = progressoService;
    }

    @GetMapping("/{id}/disponiveis")
    public ResponseEntity<ApiResponse<?>> getFaseDisponivel(@PathVariable Long id) {
        Progresso progresso = progressoService.getFaseDisponivel(id);
        ProgressoResponseDTO dto = new ProgressoResponseDTO(
                progresso.getUser().getId(),
                progresso.getCapitulo(),
                progresso.getSubtopico());
        return ResponseEntity.ok(new ApiResponse<>(true, "Progresso encontrado", dto));
    }
}
