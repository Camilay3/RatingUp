package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.config.security.TokenGenerator;
import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;

import com.quadcore.Ratingup.dto.profile.PasswordResetDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateDTO;
import com.quadcore.Ratingup.model.profile.Progresso;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressoRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final ProgressoRepository progressoRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProgressoRepository progressoRepository, TokenGenerator tokenGenerator, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.progressoRepository = progressoRepository;
        this.emailService = emailService;
    }

    @Transactional
    public User registerUser(User user) {
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

    public Optional<User> updateUser(String email, ProfileUpdateDTO data) {
        Optional<User> optionalUser = Optional.of(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        User user = optionalUser.get();

        if (data.nome() != null) {
            user.setNome(data.nome());
        }
        if (data.nickname() != null) {
            user.setNickname(data.nickname());
        }
        if (data.telefone() != null) {
            user.setTelefone(data.telefone());
        }

        userRepository.save(user);

        return Optional.of(user);
    }

    public Optional<User> deleteUser(String email) {
        Optional<User> user = Optional.of(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        user.ifPresent(userRepository::delete);

        return user;
    }

    private void updatePassword(User user, PasswordChangeDTO dto){

        if(!passwordEncoder.matches(dto.senhaAntiga(), user.getSenha())){
            throw new RuntimeException("As senhas não combinam");
        }

        String senhaPadrao = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";
        if(!dto.senhaNova().matches(senhaPadrao)){
            throw new RuntimeException("Senha nova fraca! Digite uma senha que tenha letras maiúsculas, minúsculas, números e símbolos");
        }
        user.setSenha(passwordEncoder.encode(dto.senhaNova()));
    }

    public String loginUser(String email, String senha){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(passwordEncoder.matches(senha, user.getSenha())){
            return tokenGenerator.gerarToken(user);
        }

        throw new RuntimeException("Senha inválida");
    }

    public void PasswordRecoverRequest(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("E-mail não encontrado"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        emailService.sendRecoverMail(user.getEmail(), token);
    }
    public void resetPassword(PasswordResetDTO dto){
        User user = userRepository.findByResetToken(dto.token())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if(user.getResetTokenExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Esse token está expirado");
        }

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";
        if(!dto.newPassword().matches(regex)){
            throw new RuntimeException("Senha nova fraca! Digite uma senha que tenha letras maiúsculas, minúsculas, números e símbolos");
        }

        user.setSenha(passwordEncoder.encode(dto.newPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

    }
}
