package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.board.QuizAnswerRequestDTO;
import com.quadcore.Ratingup.dto.board.QuizAnswerResultDTO;
import com.quadcore.Ratingup.dto.board.QuizResponseDTO;
import com.quadcore.Ratingup.dto.book.SubtopicIdRequestDto;
import com.quadcore.Ratingup.service.MultipleChoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Quiz", description = "Endpoints para questões de múltipla escolha")
@RequestMapping("/move")
@RestController
public class MultipleChoiceController {

    @Autowired
    private MultipleChoiceService multipleChoiceService;

    @PostMapping("/session/quiz")
    public ResponseEntity<QuizResponseDTO> getQuiz(@RequestBody @Valid SubtopicIdRequestDto subtopicIdRequestDto) {
        return ResponseEntity.ok(multipleChoiceService.getQuiz(subtopicIdRequestDto.subtopicId()));
    }

    @PostMapping("/session/quiz/answer")
    public ResponseEntity<QuizAnswerResultDTO> answerQuiz(@RequestBody @Valid QuizAnswerRequestDTO dto) {
        return ResponseEntity.ok(multipleChoiceService.answerQuiz(dto));
    }
}