package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.images.Images;
import com.quadcore.Ratingup.repository.ImagesRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImagesService {

    private final ImagesRepository imagesRepository;
    private final MinioClient minioClient;

    public ImagesService(ImagesRepository imagesRepository, MinioClient minioClient) {
        this.imagesRepository = imagesRepository;
        this.minioClient = minioClient;
    }

    public Images upload(MultipartFile file, String bucketName) throws Exception {
        var inputStream = file.getInputStream();
        var objectId = UUID.randomUUID().toString();
        var imageName = file.getOriginalFilename()
                .replace(" ", "-");

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectId)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType("image/png")
                        .build()
        );

        Images image = new Images(objectId, imageName, bucketName);

        return imagesRepository.save(image);
    }

    public byte[] getImage(String imageName, String bucketName) throws Exception {
        var image = imagesRepository.findByImageName(imageName)
                .orElseThrow(() -> new EntityNotFoundException("Imagem não encontrada"));

        var stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(image.getObjectId())
                        .build()
        );

        return IOUtils.toByteArray(stream);
    }

    public boolean exists(String originalName, String bucketName) {
        return imagesRepository.existsByOriginalNameAndBucketName(originalName, bucketName);
    }
}
