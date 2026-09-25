package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateRequestDTO;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private User mockedUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        mockedUser = new User();
        mockedUser.setId(1L);
        mockedUser.setEmail("test@test.com");
        mockedUser.setNickname("testnick");
        mockedUser.setName("Test User");
        mockedUser.setTelefone("123456789");

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
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
    void registerUser_ShouldReturn201() throws Exception {
        ProfileRequestDTO dto = new ProfileRequestDTO("Test User Name", "testnick", "test@test.com", "123456789", "Password123!");

        Mockito.when(userService.registerUser(any(ProfileRequestDTO.class))).thenReturn(mockedUser);

        mockMvc.perform(post("/conta/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Usuário cadastrado com sucesso"));
    }

    @Test
    void updateUser_ShouldReturn200() throws Exception {
        ProfileUpdateRequestDTO dto = new ProfileUpdateRequestDTO("New User Name", "newnickname", "test@test.com", "987654321");

        Mockito.when(userService.updateUser(eq("test@test.com"), any(ProfileUpdateRequestDTO.class))).thenReturn(mockedUser);

        mockMvc.perform(patch("/conta/me/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void deleteUser_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/conta/me/deletar"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser("test@test.com");
    }

    @Test
    void showUser_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/conta/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.email").value("test@test.com"));
    }
}
