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

    @org.springframework.beans.factory.annotation.Value("${app.upload.max-size:5242880}")
    private long maxUploadSize;

    public ImagesService(ImagesRepository imagesRepository, MinioClient minioClient) {
        this.imagesRepository = imagesRepository;
        this.minioClient = minioClient;
    }

    public Images upload(MultipartFile file, String bucketName) throws Exception {
        if (file.getSize() > maxUploadSize) {
            throw new IllegalArgumentException("O tamanho do arquivo excede o limite permitido.");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/webp"))) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Apenas JPEG, PNG e WEBP são suportados.");
        }

        byte[] bytes = file.getBytes();
        if (!isValidImageByMagicBytes(bytes, contentType)) {
            throw new IllegalArgumentException("Assinatura do arquivo inválida.");
        }

        var objectId = UUID.randomUUID().toString();
        
        String extension = "";
        if (contentType.equals("image/jpeg")) extension = ".jpg";
        else if (contentType.equals("image/png")) extension = ".png";
        else if (contentType.equals("image/webp")) extension = ".webp";

        var imageName = objectId + extension;

        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(bytes);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectId)
                        .stream(bais, bytes.length, -1)
                        .contentType(contentType)
                        .build()
        );

        Images image = new Images(objectId, imageName, bucketName);

        return imagesRepository.save(image);
    }

    private boolean isValidImageByMagicBytes(byte[] bytes, String contentType) {
        if (bytes.length < 12) return false;
        if ("image/jpeg".equals(contentType)) {
            return bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF;
        } else if ("image/png".equals(contentType)) {
            return bytes[0] == (byte) 0x89 && bytes[1] == (byte) 0x50 && bytes[2] == (byte) 0x4E && bytes[3] == (byte) 0x47;
        } else if ("image/webp".equals(contentType)) {
            return bytes[0] == (byte) 0x52 && bytes[1] == (byte) 0x49 && bytes[2] == (byte) 0x46 && bytes[3] == (byte) 0x46 &&
                   bytes[8] == (byte) 0x57 && bytes[9] == (byte) 0x45 && bytes[10] == (byte) 0x42 && bytes[11] == (byte) 0x50;
        }
        return false;
    }

    public byte[] getImage(String bucketName, String objectId) throws Exception {
        var stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectId)
                        .build()
        );

        return IOUtils.toByteArray(stream);
    }

    public boolean exists(String imageName, String bucketName) {
        return imagesRepository.existsByImageNameAndBucketName(imageName, bucketName);
    }
}
