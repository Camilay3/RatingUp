package com.quadcore.Ratingup.config.db;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {

    @Value("${MINIO_ROOT_USER}")
    private String accessKey;

    @Value("${MINIO_ROOT_PASSWORD}")
    private String secretKey;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://minio:9000")
                .credentials(accessKey, secretKey)
                .build();
    }
}
