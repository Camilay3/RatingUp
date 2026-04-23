package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.profile.Progresso;
import com.quadcore.Ratingup.repository.ProgressoRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProgressoService {

    private final UserRepository userRepository;
    private final ProgressoRepository progressoRepository;

    public ProgressoService(UserRepository userRepository, ProgressoRepository progressoRepository) {
        this.userRepository = userRepository;
        this.progressoRepository = progressoRepository;
    }

    public Progresso getFaseDisponivel(Long userId) {
        return progressoRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado"));
    }
}
