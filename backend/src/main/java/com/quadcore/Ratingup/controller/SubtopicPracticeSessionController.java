package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.*;
import com.quadcore.Ratingup.dto.book.SubtopicIdRequestDto;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.SubtopicPracticeSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Movimentos", description = "Endpoints para gerenciamento dos movimentos das peças")
@RequestMapping("/move")
@RestController
public class SubtopicPracticeSessionController {

    @Autowired
    private SubtopicPracticeSessionService subtopicPracticeSessionService;

    @PostMapping("/session/type")
    public ResponseEntity<SubtopicTypeResponseDto> getSubtopicType(@RequestBody @Valid SubtopicIdRequestDto subtopicIdRequestDto) {
        return ResponseEntity.ok(subtopicPracticeSessionService.getSubtopicType(subtopicIdRequestDto.subtopicId()));
    }

    @PostMapping("/session/start")
    public ResponseEntity<SubtopicPracticeSessionResponseDTO> startSession(
            @AuthenticationPrincipal User logged,
            @RequestBody SubtopicIdRequestDto dto) {
        return ResponseEntity.ok(subtopicPracticeSessionService.startSession(logged.getId(), dto.subtopicId()));
    }

    @PostMapping("/session/move")
    public ResponseEntity<SubtopicPracticeSessionResponseDTO> performSessionMove(
            @RequestBody @Valid SubtopicPracticeSessionRequestDTO moveDto) {
        return ResponseEntity.ok(subtopicPracticeSessionService.performMovement(moveDto));
    }
}
