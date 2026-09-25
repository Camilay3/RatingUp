package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.SubtopicTypeResponseDto;
import com.quadcore.Ratingup.service.SubtopicPracticeSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subtópico", description = "Endpoints para gerenciamento de subtópicos individuais")
@RequestMapping("/subtopics")
@RestController
public class SubtopicController {

    @Autowired
    private SubtopicPracticeSessionService subtopicPracticeSessionService;

    @Autowired
    private BookService bookService;

    @Operation(summary = "Busca o tipo de um subtópico")
    @GetMapping("/{id}/type")
    public ResponseEntity<SubtopicTypeResponseDto> getSubtopicType(@PathVariable("id") Long id) {
        return ResponseEntity.ok(subtopicPracticeSessionService.getSubtopicType(id));
    }

    @Operation(summary = "Busca o conteúdo de um subtópico")
    @GetMapping("/{id}/content")
    public ResponseEntity<?> getSubtopicContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Conteúdo encontrado",
                bookService.getSubtopicContent(id)));
    }
}
