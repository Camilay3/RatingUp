package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;

    public ProgressService(UserRepository userRepository, ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public Progress allowedPhases(String userEmail) {
        return progressRepository.findByUserEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));
    }

    @Transactional
    public Progress updateCurrentPhase(String userEmail, ProgressUpdateDTO dto) {
        Progress progressoUser = progressRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));

        progressoUser.setChapters(dto.chapter());
        progressoUser.setSubtopics(dto.subtopic());

        return progressRepository.save(progressoUser);
    }
}
