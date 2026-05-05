package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.book.Chapters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChaptersRepository extends JpaRepository<Chapters, Long> {

    @Query("SELECT c FROM Chapters c LEFT JOIN FETCH c.subtopics ORDER BY c.displayOrder ASC")
    List<Chapters> findAllByOrderbyOrderAsc();
}
