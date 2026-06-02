package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.images.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Optional<Images> findByImageName(String imageName);
    boolean existsByOriginalNameAndBucketName(String originalName, String bucketName);
}
