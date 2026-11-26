package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.enums.AchievementCategory;
import com.quadcore.Ratingup.model.achievement.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByCode(String code);
    List<Achievement> findByCategory(AchievementCategory category);
}
