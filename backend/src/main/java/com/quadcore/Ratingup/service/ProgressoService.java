package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.progresso.AtualizaProgressoDTO;
import com.quadcore.Ratingup.model.profile.Progresso;
import com.quadcore.Ratingup.repository.ProgressoRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProgressoService {

    private final ProgressoRepository progressoRepository;

    public ProgressoService(UserRepository userRepository, ProgressoRepository progressoRepository) {
        this.progressoRepository = progressoRepository;
    }

    public Progresso allowedPhases(String userEmail) {
        return progressoRepository.findByUserEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));
    }

    public Progresso updateCurrentPhase(String userEmail, AtualizaProgressoDTO dto) {
        Progresso progressoUser = progressoRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));

        progressoUser.setCapitulo(dto.capitulo());
        progressoUser.setSubtopico(dto.subtopico());

        return progressoRepository.save(progressoUser);
    }
}
