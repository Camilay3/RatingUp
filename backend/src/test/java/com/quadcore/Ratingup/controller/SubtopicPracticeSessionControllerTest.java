package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionRequestDTO;
import com.quadcore.Ratingup.dto.board.SubtopicPracticeSessionResponseDTO;
import com.quadcore.Ratingup.dto.board.SubtopicTypeResponseDto;
import com.quadcore.Ratingup.dto.book.SubtopicIdRequestDto;
import com.quadcore.Ratingup.enums.SubtopicType;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.SubtopicPracticeSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SubtopicPracticeSessionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubtopicPracticeSessionService service;

    @InjectMocks
    private SubtopicPracticeSessionController controller;

    private ObjectMapper objectMapper;

    private HandlerMethodArgumentResolver putAuthenticationPrincipal;

    @BeforeEach
    void setUp() {
        putAuthenticationPrincipal = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().isAssignableFrom(User.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                User user = new User();
                user.setId(1L);
                return user;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(putAuthenticationPrincipal)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should get subtopic type")
    void testGetSubtopicType() throws Exception {
        SubtopicIdRequestDto dto = new SubtopicIdRequestDto(1L);
        when(service.getSubtopicType(1L)).thenReturn(new SubtopicTypeResponseDto(1L, SubtopicType.BOARD));

        mockMvc.perform(post("/move/session/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("BOARD"));
    }

    @Test
    @DisplayName("Should start session")
    void testStartSession() throws Exception {
        SubtopicIdRequestDto dto = new SubtopicIdRequestDto(1L);
        when(service.startSession(eq(1L), eq(1L))).thenReturn(new SubtopicPracticeSessionResponseDTO(1L, "fen", "NORMAL", "initial"));

        mockMvc.perform(post("/move/session/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(1L));
    }

    @Test
    @DisplayName("Should perform move")
    void testPerformSessionMove() throws Exception {
        SubtopicPracticeSessionRequestDTO dto = new SubtopicPracticeSessionRequestDTO(1L, Piece.WHITE_PAWN, Square.A2, Square.A4);
        when(service.performMovement(any(SubtopicPracticeSessionRequestDTO.class))).thenReturn(new SubtopicPracticeSessionResponseDTO(1L, "fen2", "NORMAL", "initial"));

        mockMvc.perform(post("/move/session/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fen").value("fen2"));
    }
}
