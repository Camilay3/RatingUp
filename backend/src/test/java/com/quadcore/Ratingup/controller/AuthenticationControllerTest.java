package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.profile.LoginRequestDTO;
import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;
import com.quadcore.Ratingup.dto.profile.PasswordResetDTO;
import com.quadcore.Ratingup.dto.profile.PasswordResetRequestDTO;
import com.quadcore.Ratingup.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private ObjectMapper objectMapper;

    private UserDetails mockedUserDetails;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        mockedUserDetails = new User("test@test.com", "Pass12345!@#", Collections.emptyList());

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().isAssignableFrom(UserDetails.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return mockedUserDetails;
                    }
                })
                .build();
    }

    @Test
    void loginUser_ShouldReturn200AndSetCookie() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("test@test.com", "Pass12345!@#");

        Mockito.when(userService.loginUser(anyString(), anyString())).thenReturn("fake-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void logoutUser_ShouldReturn200AndSetEmptyCookie() throws Exception {
        mockMvc.perform(delete("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void validateToken_ShouldReturn200AndSetCookie() throws Exception {
        Mockito.when(userService.validateResetToken(anyString())).thenReturn("new-token");

        mockMvc.perform(post("/auth/validate-token")
                        .param("token", "some-token"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));
    }

    @Test
    void recoverRequest_ShouldReturn200() throws Exception {
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO("test@test.com");

        mockMvc.perform(post("/auth/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));

        Mockito.verify(userService).passwordRecoverRequest("test@test.com");
    }

    @Test
    void resetPassword_ShouldReturn200() throws Exception {
        PasswordResetDTO dto = new PasswordResetDTO("NewPass123!@");

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));

        Mockito.verify(userService).resetPassword("NewPass123!@");
    }

    @Test
    void changePassword_ShouldReturn200() throws Exception {
        PasswordChangeDTO dto = new PasswordChangeDTO("Pass12345!@#", "NewPass123!@");

        mockMvc.perform(put("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));

        Mockito.verify(userService).changePassword(eq("test@test.com"), any(PasswordChangeDTO.class));
    }
}
