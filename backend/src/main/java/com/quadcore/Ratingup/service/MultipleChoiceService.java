package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.exception.*;
import com.quadcore.Ratingup.dto.board.QuizAnswerRequestDTO;
import com.quadcore.Ratingup.dto.board.QuizAnswerResultDTO;
import com.quadcore.Ratingup.dto.board.QuizOptionDTO;
import com.quadcore.Ratingup.dto.board.QuizResponseDTO;
import com.quadcore.Ratingup.model.board.MultipleChoiceOption;
import com.quadcore.Ratingup.model.board.MultipleChoiceQuestion;
import com.quadcore.Ratingup.repository.MultipleChoiceOptionRepository;
import com.quadcore.Ratingup.repository.MultipleChoiceQuestionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MultipleChoiceService {

    private final MultipleChoiceQuestionRepository questionRepository;

    private final MultipleChoiceOptionRepository optionRepository;

    public QuizResponseDTO getQuiz(Long subtopicId) {
        MultipleChoiceQuestion question = questionRepository.findBySubtopicId(subtopicId)
                .orElseThrow(() -> new ResourceNotFoundException("Questão não encontrada para esse subtópico"));

        List<QuizOptionDTO> options = question.getOptions().stream()
                .map(opt -> new QuizOptionDTO(opt.getId(), opt.getOptionText()))
                .toList();

        return new QuizResponseDTO(subtopicId, question.getQuestionText(), options);
    }

    public QuizAnswerResultDTO answerQuiz(QuizAnswerRequestDTO dto) {
        MultipleChoiceOption selected = optionRepository.findById(dto.selectedOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Opção não encontrada"));

        // Garante que a opção pertence ao subtópico correto
        if (!selected.getQuestion().getSubtopic().getId().equals(dto.subtopicId())) {
            throw new BusinessRuleViolationException("Opção não pertence a esse subtópico");
        }

        return new QuizAnswerResultDTO(selected.getIsCorrect());
    }
}
