package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("""
        SELECT COUNT(g) > 0 FROM Game g WHERE g.id = :id
        AND (g.whitePlayerId = :userId OR g.blackPlayerId = :userId)
        """)
    boolean isParticipant(@Param("id") Long id, @Param("userId") Long userId);

    List<Game> findTop20ByStatusAndWhitePlayerIdNotOrderByIdDesc(GameStatus status, Long whitePlayerId);

    @Query("""
            SELECT g FROM Game g WHERE g.status <> :status
            AND (g.whitePlayerId = :userId OR g.blackPlayerId = :userId)
            ORDER BY g.id DESC
            """)
    List<Game> findByPlayerAndStatusNot(Long userId, GameStatus status);
}