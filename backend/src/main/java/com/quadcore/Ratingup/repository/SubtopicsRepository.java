package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.book.Subtopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubtopicsRepository extends JpaRepository<Subtopics, Long> {
    List<Subtopics> findByChapter_IdOrderByDisplayOrderAsc(Long chapterId);
    
    @Modifying
    @Query("UPDATE Subtopics s SET s.displayOrder = s.displayOrder + 1 WHERE s.chapter.id = :chapterId AND s.displayOrder >= :displayOrder")
    void incrementDisplayOrderFrom(@Param("chapterId") Long chapterId, @Param("displayOrder") Integer displayOrder);

    Optional<Subtopics> findTopByChapterIdOrderByDisplayOrderDesc(Integer chapterId);
}
