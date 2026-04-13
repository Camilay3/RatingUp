package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<User, Long> {
}
