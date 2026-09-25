package com.quadcore.Ratingup.controller;
import com.quadcore.Ratingup.enums.Roles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.AdminUserService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminUserService adminUserService;

    @InjectMocks
    private AdminUserController adminUserController;

    private ObjectMapper objectMapper;

    private User mockedAdmin;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        mockedAdmin = new User();
        mockedAdmin.setId(1L);
        mockedAdmin.setEmail("admin@test.com");
        mockedAdmin.setNickname("adminnick");
        mockedAdmin.setName("Admin User Name");
        mockedAdmin.setTelefone("123456789");

        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().isAssignableFrom(User.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return mockedAdmin;
                    }
                })
                .build();
    }

    @Test
    void listUsers_ShouldReturn200() throws Exception {
        ProfileResponseDTO dto = new ProfileResponseDTO(1L, "Admin", "nick", "admin@test.com", "123456789", Roles.ADMIN, LocalDateTime.now(), "avatar");
        Mockito.when(adminUserService.listUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/admin/contas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].email").value("admin@test.com"));
    }

    @Test
    void searchUserById_ShouldReturn200() throws Exception {
        Mockito.when(adminUserService.searchUserById(1L)).thenReturn(mockedAdmin);

        mockMvc.perform(get("/admin/contas/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.email").value("admin@test.com"));
    }

    @Test
    void registerAdminUser_ShouldReturn201() throws Exception {
        ProfileRequestDTO dto = new ProfileRequestDTO("Admin User Name", "adminnick", "admin@test.com", "123456789", "Pass123!@#");

        Mockito.when(adminUserService.registerAdminUser(any(User.class))).thenReturn(mockedAdmin);

        mockMvc.perform(post("/admin/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.email").value("admin@test.com"));
    }

    @Test
    void showUserAdmin_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/admin/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.email").value("admin@test.com"));
    }
}
