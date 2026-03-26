package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User makeUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByID(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Object> updateUser(Long id, User data) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        if (data.getNome() != null) {
            user.setNome(data.getNome());
        }

        if (data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }

        userRepository.save(user);

        return Optional.of(user);
    }
}
