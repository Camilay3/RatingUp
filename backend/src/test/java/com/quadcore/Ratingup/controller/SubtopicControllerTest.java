package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.board.SubtopicTypeResponseDto;
import com.quadcore.Ratingup.enums.SubtopicType;
import com.quadcore.Ratingup.service.SubtopicPracticeSessionService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SubtopicControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubtopicPracticeSessionService service;

    @InjectMocks
    private SubtopicController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Should get subtopic type")
    void testGetSubtopicType() throws Exception {
        when(service.getSubtopicType(1L)).thenReturn(new SubtopicTypeResponseDto(1L, SubtopicType.BOARD));

        mockMvc.perform(get("/subtopics/1/type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("BOARD"));
    }

    @Mock
    private com.quadcore.Ratingup.service.BookService bookService;

    @Test
    @DisplayName("Should get subtopic content")
    void testGetSubtopicContent() throws Exception {
        com.quadcore.Ratingup.dto.book.SubtopicResponseDTO responseDTO = new com.quadcore.Ratingup.dto.book.SubtopicResponseDTO(1L, "Content", 1);
        when(bookService.getSubtopicContent(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/subtopics/1/content")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Content"));
    }
}
