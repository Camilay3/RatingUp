package com.quadcore.Ratingup.repository;

import com.quadcore.Ratingup.model.board.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MultipleChoiceQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {
    Optional<MultipleChoiceQuestion> findBySubtopicId(Long subtopicId);
}