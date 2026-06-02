package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.ImagesService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImagesController {

    private final ImagesService imagesService;

    public ImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @PostMapping(value = "/upload/{bucketName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> upload(
            @RequestPart("file")
            @Schema(type = "string", format = "binary")
            MultipartFile file,
            @PathVariable String bucketName) throws Exception {
            imagesService.upload(file, bucketName);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Imagem subiu com sucesso",
                            null));
        }

    @GetMapping(value = "/{bucketName}/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName, @PathVariable String bucketName) throws Exception {
        byte[] image = imagesService.getImage(imageName, bucketName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @GetMapping("/exists/{bucketName}/{originalName}")
    public ResponseEntity<ApiResponse<?>> exists(
            @PathVariable String bucketName,
            @PathVariable String originalName) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, imagesService.exists(originalName, bucketName)));
    }
}
