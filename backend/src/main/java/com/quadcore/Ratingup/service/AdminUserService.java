package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.profile.Progresso;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressoRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final ProgressoRepository progressoRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, ProgressoRepository progressoRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.progressoRepository = progressoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listUsers() { return userRepository.findAll();}

    public User searchUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public User registerAdminUser(User user) {
        String senhaPadrao = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";

        if(user.getSenha() == null || user.getSenha().isEmpty()){
            throw new RuntimeException("A senha não pode ser nula");
        }
        if(!user.getSenha().matches(senhaPadrao)){
            throw new RuntimeException("Senha inválida! A senha precisa ter entre 8 a 12 caracteres, que tenham letras maiúsculas, minúsculas, números e símbolos");
        }

        String senhaCriptografada = passwordEncoder.encode(user.getSenha());
        user.setSenha(senhaCriptografada);

        User savedUser = userRepository.save(user);
        Progresso progresso = new Progresso();
        progresso.setUser(savedUser);
        savedUser.setProgresso(progresso);
        progressoRepository.save(progresso);

        return savedUser;
    }
}
