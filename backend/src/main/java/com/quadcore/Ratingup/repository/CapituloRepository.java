package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.book.Capitulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CapituloRepository extends JpaRepository<Capitulo, Long> {

    @Query("SELECT c FROM Capitulo c LEFT JOIN FETCH c.subtopicos ORDER BY c.ordem ASC")
    List<Capitulo> findAllByOrderbyOrdemAsc();
}
