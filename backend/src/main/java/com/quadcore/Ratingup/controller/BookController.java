package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livro")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/paginas")
    public ResponseEntity<ApiResponse<?>> listPages() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Páginas encontradas", bookService.buildBook()));
    }
}
