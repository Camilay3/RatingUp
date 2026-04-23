package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
