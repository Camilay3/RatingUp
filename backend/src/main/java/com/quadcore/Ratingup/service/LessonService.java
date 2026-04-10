package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LessonService {

    private final UserRepository userRepository;

    public LessonService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> atualizaFaseAtual(Long id, User data) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        user.setFaseAtual(data.getFaseAtual());
        userRepository.save(user);

        return Optional.of(user);
    }
}
