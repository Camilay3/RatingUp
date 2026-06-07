package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.book.SubtopicRequestDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Livro", description = "Endpoint para gerenciamento do livro")
@RestController
@RequestMapping("/livro")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Lista as páginas do livro")
    @GetMapping("/paginas")
    public ResponseEntity<ApiResponse<?>> listPages() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Páginas encontradas",
                        bookService.buildBook()));
    }

    @Operation(summary = "Adiciona mais subtópicos ao livro")
    @PostMapping("/adicionar")
    public ResponseEntity<ApiResponse<?>> addSubtopics(@Valid @RequestBody SubtopicRequestDTO dto) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Subtópico salvo com sucesso",
                        bookService.addSubtopic(dto)));
    }

    @Operation(summary = "Busca o conteúdo de um subtópico")
    @GetMapping("/subtopico/{id}")
    public ResponseEntity<ApiResponse<?>> getSubtopicContent(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Conteúdo encontrado",
                        bookService.getSubtopicContent(id)));
    }
}
