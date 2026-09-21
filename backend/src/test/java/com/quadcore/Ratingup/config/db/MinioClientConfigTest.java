package com.quadcore.Ratingup.config.db;

import io.minio.MinioClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MinioClientConfigTest {

    @Test
    @DisplayName("Should initialize MinioClient")
    void testMinioClient() {
        MinioClientConfig config = new MinioClientConfig();
        ReflectionTestUtils.setField(config, "accessKey", "admin");
        ReflectionTestUtils.setField(config, "secretKey", "password");
        
        MinioClient client = config.minioClient();
        assertNotNull(client);
    }
}
