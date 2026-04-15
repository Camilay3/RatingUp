package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.Progresso;
import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.repository.ProgressoRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ProgressoRepository progressoRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProgressoRepository progressoRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.progressoRepository = progressoRepository;
    }

    @Transient
    public User criarUsuario(User user) {
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
