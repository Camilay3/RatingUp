package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.images.Images;
import com.quadcore.Ratingup.repository.ImagesRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImagesServiceTest {

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private ImagesService imagesService;

    @Test
    @DisplayName("Should upload image successfully")
    void testUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test content".getBytes());
        Images savedImage = new Images("objectId", "test.png", "test-bucket");

        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(null);
        when(imagesRepository.save(any(Images.class))).thenReturn(savedImage);

        Images result = imagesService.upload(file, "test-bucket");

        assertNotNull(result);
        assertEquals("test-bucket", result.getBucketName());
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
        verify(imagesRepository, times(1)).save(any(Images.class));
    }

    @Test
    @DisplayName("Should get image successfully")
    void testGetImage() throws Exception {
        byte[] content = "test image content".getBytes();
        InputStream stream = new ByteArrayInputStream(content);
        
        // Mock the GetObjectResponse which extends FilterInputStream (or similar depending on MinIO version)
        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        lenient().when(getObjectResponse.readAllBytes()).thenReturn(content);
        lenient().when(getObjectResponse.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invocation -> stream.read((byte[]) invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2)));
        lenient().when(getObjectResponse.read(any(byte[].class))).thenAnswer(invocation -> stream.read((byte[]) invocation.getArgument(0)));
        lenient().when(getObjectResponse.read()).thenAnswer(invocation -> stream.read());
        
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(getObjectResponse);

        byte[] result = imagesService.getImage("test-bucket", "test-object");

        assertNotNull(result);
        assertArrayEquals(content, result);
    }

    @Test
    @DisplayName("Should check if image exists")
    void testExists() {
        when(imagesRepository.existsByImageNameAndBucketName("test-image.png", "test-bucket")).thenReturn(true);

        boolean result = imagesService.exists("test-image.png", "test-bucket");

        assertTrue(result);
    }
}
