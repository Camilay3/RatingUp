package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
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

    public User buscarPorID(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Optional<User> atualizarUsuario(Long id, User data) {
        Optional<User> optionalUser = Optional.of(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        User user = optionalUser.get();

        if (data.getNome() != null) {
            user.setNome(data.getNome());
        }
        if (data.getNickname() != null) {
            user.setNickname(data.getNickname());
        }
        if (data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }
        if (data.getTelefone() != null) {
            user.setTelefone(data.getTelefone());
        }

        userRepository.save(user);

        return Optional.of(user);
    }

    public Optional<User> deletarUsuario(Long id) {
        Optional<User> user = Optional.of(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        user.ifPresent(userRepository::delete);

        return user;
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }
}
