package com.quadcore.Ratingup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadcore.Ratingup.service.ImagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ImagesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImagesService imagesService;

    @InjectMocks
    private ImagesController imagesController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imagesController).build();
    }

    @Test
    @DisplayName("Should upload image successfully")
    void testUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test content".getBytes());

        mockMvc.perform(multipart("/images/upload/test-bucket")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Imagem subiu com sucesso"));
    }

    @Test
    @DisplayName("Should get image successfully")
    void testGetImage() throws Exception {
        byte[] imageContent = "test image".getBytes();
        when(imagesService.getImage("test-bucket", "test-object-id")).thenReturn(imageContent);

        mockMvc.perform(get("/images/test-bucket/test-object-id")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @DisplayName("Should check if image exists")
    void testExists() throws Exception {
        when(imagesService.exists("test-image.png", "test-bucket")).thenReturn(true);

        mockMvc.perform(get("/images/exists/test-bucket/test-image.png"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }
}
