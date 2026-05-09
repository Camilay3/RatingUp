package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.book.Subtopics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtopicsRepository extends JpaRepository<Subtopics, Long> {
    List<Subtopics> findByChapter_IdOrderByDisplayOrderAsc(Long chapterId);
}
