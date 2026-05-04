package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.profile.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressoRepository extends JpaRepository<Progresso, Long> {
    Optional<Progresso> findByUserId(Long userId);
    Optional<Progresso> findByUserEmail(String email);
}
