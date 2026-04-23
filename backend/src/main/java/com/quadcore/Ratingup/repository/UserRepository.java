package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
