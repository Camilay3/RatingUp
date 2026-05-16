package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, ProgressRepository progressRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ProfileResponseDTO> listUsers() {
        List<ProfileResponseDTO> dtos = userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();

        return dtos;
    }

    public User searchUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public User registerAdminUser(User user) {
        String senhaPadrao = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            throw new RuntimeException("A senha não pode ser nula");
        }
        if(!user.getPassword().matches(senhaPadrao)){
            throw new RuntimeException("Senha inválida! A senha precisa ter entre 8 a 12 caracteres, que tenham letras maiúsculas, minúsculas, números e símbolos");
        }

        String senhaCriptografada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCriptografada);
        user.setRole(Roles.ADMIN);

        User savedUser = userRepository.save(user);
        Progress progresso = new Progress();
        progresso.setUser(savedUser);
        savedUser.setProgress(progresso);
        progressRepository.save(progresso);

        return savedUser;
    }
}
