package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionRequestDTO;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.service.SubtopicPracticeSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Movimentos", description = "Endpoints para gerenciamento dos movimentos das peças")
@RequestMapping("/move")
@RestController
public class SubtopicPracticeSessionController {

    @Autowired
    private SubtopicPracticeSessionService subtopicPracticeSessionService;

    @PostMapping("/session/start")
    public ResponseEntity<SubtopicPracticeSessionResponseDTO> startSession(
            @RequestParam Long userId,
            @RequestParam Long subtopicId) {
        return ResponseEntity.ok(subtopicPracticeSessionService.startSession(userId, subtopicId));
    }

    @PostMapping("/session/{sessionId}/move")
    public ResponseEntity<SubtopicPracticeSessionResponseDTO> performSessionMove(
            @PathVariable Long sessionId,
            @RequestBody @Valid SubtopicPracticeSessionRequestDTO moveDto) {
        return ResponseEntity.ok(subtopicPracticeSessionService.performMovement(sessionId, moveDto));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<SubtopicPracticeSessionResponseDTO> getSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(subtopicPracticeSessionService.getSession(sessionId));
    }
}
