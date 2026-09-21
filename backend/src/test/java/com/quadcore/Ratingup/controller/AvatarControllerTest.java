package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.dto.profile.AvatarResponseDTO;
import com.quadcore.Ratingup.dto.profile.AvatarSelectionDTO;
import com.quadcore.Ratingup.service.AvatarService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AvatarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AvatarService avatarService;

    @InjectMocks
    private AvatarController avatarController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(avatarController).build();
    }

    @Test
    void showAvatarList_ShouldReturn200() throws Exception {
        AvatarResponseDTO avatar = new AvatarResponseDTO("avatar1.png", "obj-123");
        Mockito.when(avatarService.listAvaliableImages()).thenReturn(List.of(avatar));

        mockMvc.perform(get("/avatar/avatar-list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].imageName").value("avatar1.png"));
    }

    @Test
    void updateAvatar_ShouldReturn200() throws Exception {
        AvatarSelectionDTO dto = new AvatarSelectionDTO("avatar1.png");

        mockMvc.perform(patch("/avatar/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.imageName").value("avatar1.png"));

        Mockito.verify(avatarService).selectAvatar("avatar1.png");
    }
}
