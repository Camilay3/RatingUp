package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.enums.TimeControlCategory;
import com.quadcore.Ratingup.model.game.PlayerRating;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRatingRepository extends JpaRepository<PlayerRating, Long> {

    List<PlayerRating> findByUserId(Long userId);

    Optional<PlayerRating> findByUserIdAndCategory(Long userId, TimeControlCategory category);

    /** Cria a linha com o rating inicial informado se ainda não existir (seguro contra corrida). */
    @Modifying(flushAutomatically = true)
    @Query(value = """
        INSERT INTO player_ratings (user_id, category, rating) VALUES (:userId, :category, :rating)
        ON CONFLICT (user_id, category) DO NOTHING
        """, nativeQuery = true)
    void insertIfAbsent(@Param("userId") Long userId,
                        @Param("category") String category,
                        @Param("rating") int rating);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PlayerRating p WHERE p.userId = :userId AND p.category = :category")
    Optional<PlayerRating> findForUpdate(@Param("userId") Long userId,
                                         @Param("category") TimeControlCategory category);
}