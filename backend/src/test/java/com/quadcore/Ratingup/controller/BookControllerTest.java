package com.quadcore.Ratingup.controller;
import com.quadcore.Ratingup.dto.book.BookDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.book.SubtopicIdRequestDto;
import com.quadcore.Ratingup.dto.book.SubtopicRequestDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should list pages successfully")
    void testListPages() throws Exception {
        when(bookService.buildBook()).thenReturn(new BookDTO(List.of(), 0));

        mockMvc.perform(get("/livro/paginas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Páginas encontradas"));
    }

    @Test
    @DisplayName("Should add subtopics successfully")
    void testAddSubtopics() throws Exception {
        SubtopicRequestDTO dto = new SubtopicRequestDTO(1L, "Title", 1);
        
        mockMvc.perform(post("/livro/adicionar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Subtópico salvo com sucesso"));
    }

    @Test
    @DisplayName("Should get subtopic content successfully")
    void testGetSubtopicContent() throws Exception {
        SubtopicIdRequestDto dto = new SubtopicIdRequestDto(1L);

        when(bookService.getSubtopicContent(1L)).thenReturn(null);

        mockMvc.perform(post("/livro/subtopico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Conteúdo encontrado"));
    }
}
