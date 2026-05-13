package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.repository.ChaptersRepository;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final SubtopicsRepository subtopicsRepository;
    private final ChaptersRepository chaptersRepository;

    public ProgressService(ProgressRepository progressRepository, SubtopicsRepository subtopicsRepository, ChaptersRepository chaptersRepository) {
        this.progressRepository = progressRepository;
        this.subtopicsRepository = subtopicsRepository;
        this.chaptersRepository = chaptersRepository;
    }

    public Progress allowedPhases(String userEmail) {
        return progressRepository.findByUserEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));
    }

    public Optional<Progress> updateCurrentPhase(String userEmail, ProgressUpdateDTO dto) {
        Progress progressoUser = progressRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));

        if (dto.chapter() != progressoUser.getChapters() ||
                dto.subtopic() != progressoUser.getSubtopics()) {
            throw new IllegalArgumentException("Progresso inválido");
        }

        Subtopics lastSubtopic = subtopicsRepository
                .findTopByChapterIdOrderByDisplayOrderDesc(dto.chapter())
                .orElseThrow(() -> new EntityNotFoundException("Capítulo não encontrado"));
        int maxSubtopic = lastSubtopic.getDisplayOrder();

        if (dto.subtopic() < maxSubtopic) {
            progressoUser.setSubtopics(progressoUser.getSubtopics() + 1);
        } else {
            progressoUser.setChapters(progressoUser.getChapters() + 1);
            progressoUser.setSubtopics(1);
        }

        return Optional.of(progressRepository.save(progressoUser));
    }
}
