package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.board.QuizAnswerRequestDTO;
import com.quadcore.Ratingup.dto.board.QuizAnswerResultDTO;
import com.quadcore.Ratingup.dto.board.QuizResponseDTO;
import com.quadcore.Ratingup.model.board.MultipleChoiceOption;
import com.quadcore.Ratingup.model.board.MultipleChoiceQuestion;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.repository.MultipleChoiceOptionRepository;
import com.quadcore.Ratingup.repository.MultipleChoiceQuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MultipleChoiceServiceTest {

    @Mock
    private MultipleChoiceQuestionRepository questionRepository;

    @Mock
    private MultipleChoiceOptionRepository optionRepository;

    @InjectMocks
    private MultipleChoiceService multipleChoiceService;

    @Test
    @DisplayName("Should get quiz successfully")
    void testGetQuiz() {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        question.setQuestionText("Test Question");
        MultipleChoiceOption option = new MultipleChoiceOption();
        option.setId(1L);
        option.setOptionText("Option 1");
        question.setOptions(List.of(option));

        when(questionRepository.findBySubtopicId(1L)).thenReturn(Optional.of(question));

        QuizResponseDTO result = multipleChoiceService.getQuiz(1L);

        assertNotNull(result);
        assertEquals("Test Question", result.questionText());
        assertEquals(1, result.options().size());
    }

    @Test
    @DisplayName("Should throw when getting quiz and not found")
    void testGetQuizNotFound() {
        when(questionRepository.findBySubtopicId(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> multipleChoiceService.getQuiz(1L));
    }

    @Test
    @DisplayName("Should answer quiz successfully")
    void testAnswerQuiz() {
        QuizAnswerRequestDTO dto = new QuizAnswerRequestDTO(1L, 1L);
        MultipleChoiceOption option = new MultipleChoiceOption();
        option.setIsCorrect(true);
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        Subtopics subtopic = new Subtopics();
        subtopic.setId(1L);
        question.setSubtopic(subtopic);
        option.setQuestion(question);

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        QuizAnswerResultDTO result = multipleChoiceService.answerQuiz(dto);

        assertTrue(result.correct());
    }

    @Test
    @DisplayName("Should throw when answering quiz with option from different subtopic")
    void testAnswerQuizWrongSubtopic() {
        QuizAnswerRequestDTO dto = new QuizAnswerRequestDTO(1L, 2L);
        MultipleChoiceOption option = new MultipleChoiceOption();
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        Subtopics subtopic = new Subtopics();
        subtopic.setId(1L);
        question.setSubtopic(subtopic);
        option.setQuestion(question);

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        assertThrows(Exception.class, () -> multipleChoiceService.answerQuiz(dto));
    }
}
