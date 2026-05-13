package com.quadcore.Ratingup.controller;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class UploadController {

    private final MinioClient minioClient;
    private final JdbcClient jdbcClient;

    public UploadController(MinioClient minioClient, JdbcClient jdbcClient) {
        this.minioClient = minioClient;
        this.jdbcClient = jdbcClient;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(
            @RequestPart("file")
            @Schema(type = "string", format = "binary")
            MultipartFile file) throws Exception {
        var inputStream = file.getInputStream();
        var objectId = UUID.randomUUID().toString();
        var imageName = file.getOriginalFilename()
                .replace(" ", "_");

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("book-images")
                        .object(objectId)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType("image/png")
                        .build()
        );

        jdbcClient.sql("""
                    INSERT INTO images (
                    object_id,
                    image_name)
                    VALUES (
                    :objectId,
                    :imageName
                    )
                """)
                .param("objectId", objectId)
                .param("imageName", imageName)
                .update();
    }

    @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable String imageName) throws Exception {

        var objectId = jdbcClient.sql("""
                    SELECT object_id
                    FROM images
                    WHERE image_name = :imageName
                """)
                .param("imageName", imageName)
                .query(String.class)
                .single();

        var stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("book-images")
                        .object(objectId)
                        .build()
        );

        return IOUtils.toByteArray(stream);
    }

}
