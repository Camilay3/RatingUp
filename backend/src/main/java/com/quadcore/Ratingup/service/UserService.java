package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User criarUsuario(User user) {
        String senhaPadrao = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";

        if(user.getSenha() == null || user.getSenha().isEmpty()){
            throw new RuntimeException("A senha não pode ser nula");
        }
        if(!user.getSenha().matches(senhaPadrao)){
            throw new RuntimeException("Senha inválida! A senha precisa ter entre 8 a 12 caracteres, que tenham letras maiúsculas, minúsculas, números e símbolos");
        }

        System.out.println("Senha antes: " + user.getSenha());
        String senhaCriptografada = passwordEncoder.encode(user.getSenha());
        user.setSenha(senhaCriptografada);
        System.out.println("Senha depois:" + user.getSenha());
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
