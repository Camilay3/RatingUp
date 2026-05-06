package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.profile.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByUserId(Long userId);
    Optional<Progress> findByUserEmail(String email);
}
