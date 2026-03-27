package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User criarUsuario(User user) {
        return userRepository.save(user);
    }

    public Optional<User> buscarPorID(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> atualizarUsuario(Long id, User data) {
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

    public Optional<User> deletarUsuario(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.delete(user.get());
        }

        return user;
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }
}
