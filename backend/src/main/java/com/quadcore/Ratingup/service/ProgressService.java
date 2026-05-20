package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.progresso.ProgressResponseDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.mapper.ProgressoMapper;
import com.quadcore.Ratingup.model.book.Chapters;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.repository.ChaptersRepository;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

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

    public ProgressResponseDTO allowedPhases(String userEmail) {
        Progress progress = progressRepository.findByUserEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));
        ProgressResponseDTO dto = new ProgressResponseDTO(
                progress.getUser().getId(),
                progress.getChapters(),
                progress.getSubtopics());

        return dto;
    }

    @Transactional
    public Optional<Progress> updateCurrentPhase(String userEmail, ProgressUpdateDTO dto) {
        Progress progressoUser = progressRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));

        Chapters lastChapter = chaptersRepository
                .findTopByOrderByDisplayOrderDesc()
                .orElseThrow(() -> new EntityNotFoundException("Capítulo não encontrado"));
        int maxChapter = lastChapter.getDisplayOrder();

        Subtopics lastSubtopic = subtopicsRepository
                .findTopByChapterIdOrderByDisplayOrderDesc(dto.chapter())
                .orElseThrow(() -> new EntityNotFoundException("Capítulo não encontrado"));
        int maxSubtopic = lastSubtopic.getDisplayOrder();

        if (dto.chapter() == maxChapter &&
            dto.subtopic() == maxSubtopic) {
            throw new IllegalArgumentException("Progresso máximo alcançado!");
        }
        else if (dto.chapter() != progressoUser.getChapters() ||
                dto.subtopic() != progressoUser.getSubtopics()) {
            throw new IllegalArgumentException("Progresso inválido");
        }

        if (dto.subtopic() < maxSubtopic) {
            progressoUser.setSubtopics(progressoUser.getSubtopics() + 1);
        } else {
            progressoUser.setChapters(progressoUser.getChapters() + 1);
            progressoUser.setSubtopics(1);
        }

        Progress progress = progressRepository.save(progressoUser);

        return ProgressoMapper.toResponse(progress);
    }
}
