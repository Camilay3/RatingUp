package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.board.QuizAnswerRequestDTO;
import com.quadcore.Ratingup.dto.board.QuizAnswerResultDTO;
import com.quadcore.Ratingup.dto.board.QuizResponseDTO;
import com.quadcore.Ratingup.dto.book.SubtopicIdRequestDto;
import com.quadcore.Ratingup.service.MultipleChoiceService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MultipleChoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MultipleChoiceService multipleChoiceService;

    @InjectMocks
    private MultipleChoiceController multipleChoiceController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(multipleChoiceController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should get quiz successfully")
    void testGetQuiz() throws Exception {
        SubtopicIdRequestDto dto = new SubtopicIdRequestDto(1L);
        QuizResponseDTO responseDTO = new QuizResponseDTO(1L, "Question?", List.of());

        when(multipleChoiceService.getQuiz(1L)).thenReturn(responseDTO);

        mockMvc.perform(post("/move/session/quiz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText").value("Question?"));
    }

    @Test
    @DisplayName("Should answer quiz successfully")
    void testAnswerQuiz() throws Exception {
        QuizAnswerRequestDTO dto = new QuizAnswerRequestDTO(1L, 1L);
        QuizAnswerResultDTO responseDTO = new QuizAnswerResultDTO(true);

        when(multipleChoiceService.answerQuiz(any(QuizAnswerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/move/session/quiz/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));
    }
}
