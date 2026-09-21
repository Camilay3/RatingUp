package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.progresso.ProgressResponseDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.ProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProgressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private ProgressController progressController;

    private ObjectMapper objectMapper;

    private User mockedUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        mockedUser = new User();
        mockedUser.setId(1L);
        mockedUser.setEmail("test@test.com");

        mockMvc = MockMvcBuilders.standaloneSetup(progressController)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().isAssignableFrom(User.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return mockedUser;
                    }
                })
                .build();
    }

    @Test
    void allowedPhases_ShouldReturn200() throws Exception {
        ProgressResponseDTO responseDTO = new ProgressResponseDTO(1L, 2, 1);
        Mockito.when(progressService.allowedPhases("test@test.com")).thenReturn(responseDTO);

        mockMvc.perform(get("/progresso/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.chapter").value(2))
                .andExpect(jsonPath("$.data.subtopic").value(1));
    }

    @Test
    void updateCurrentPhase_ShouldReturn200() throws Exception {
        ProgressUpdateDTO dto = new ProgressUpdateDTO(1, 1);
        ProgressResponseDTO responseDTO = new ProgressResponseDTO(1L, 1, 2);

        Mockito.when(progressService.updateCurrentPhase(eq("test@test.com"), any(ProgressUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/progresso/atualiza-fase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.subtopic").value(2));
    }
}
