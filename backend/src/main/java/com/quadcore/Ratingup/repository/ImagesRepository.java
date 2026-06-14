package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.images.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Optional<Images> findByImageName(String imageName);
    boolean existsByImageNameAndBucketName(String imageName, String bucketName);
    List<Images> findAllByBucketName(String bucketName);
}
