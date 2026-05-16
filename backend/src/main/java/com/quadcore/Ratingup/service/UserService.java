package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.config.security.TokenGenerator;
import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;

import com.quadcore.Ratingup.dto.profile.PasswordResetDTO;
import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateRequestDTO;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.handler.DuplicateFieldException;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final ProgressRepository progressRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProgressRepository progressRepository, TokenGenerator tokenGenerator, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.progressRepository = progressRepository;
        this.emailService = emailService;
    }

    @Transactional
    public User registerUser(ProfileRequestDTO dto) {

        List<String> errors = new ArrayList<>();
        if(userRepository.existsByEmail(dto.email())){
            errors.add("E-email já cadastrado");
        }
        if(userRepository.existsByNickname(dto.nickname())){
            errors.add("Nickname já cadastrado");
        }
        if(userRepository.existsByTelefone(dto.telefone())){
            errors.add("telefone já cadastrado");
        }
        if (!errors.isEmpty()) {
            throw new DuplicateFieldException(errors);
        }

        User user = UserMapper.toEntity(dto);
        user.setRole(Roles.USER);

        String senhaCriptografada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCriptografada);

        //talvez fazer uma função so pra isso aq(mateus)
        User savedUser = userRepository.save(user);
        Progress progresso = new Progress();
        progresso.setUser(savedUser);
        savedUser.setProgress(progresso);
        progressRepository.save(progresso);

        return savedUser;
    }

    @Transactional
    public User updateUser(String email, ProfileUpdateRequestDTO data) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum usuário encontrado para esse email"));

        if (data.name() != null) {
            user.setName(data.name());
        }
        if (data.nickname() != null) {
            user.setNickname(data.nickname());
        }
        if (data.telefone() != null) {
            user.setTelefone(data.telefone());
        }

        userRepository.saveAndFlush(user);

        return user;
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        userRepository.deleteById(user.getId());
    }

    public void changePassword(String email, PasswordChangeDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum Usuário encontrado para esse email"));

        if(!passwordEncoder.matches(dto.oldPassword(), user.getPassword())){
            throw new RuntimeException("Senha antiga não está correta");
        }

        if(dto.oldPassword().equals(dto.newPassword())){
            throw new RuntimeException("A nova senha não pode ser igual a antiga");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    public ResponseCookie loginUser(String email, String senha){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado para esse email"));

        if(!(passwordEncoder.matches(senha, user.getPassword()))) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = tokenGenerator.gerarToken(user);

        return ResponseCookie
                .from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(7))
                .build();
    }

    public ResponseCookie logoutUser() {
        return ResponseCookie
                .from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();
    }

    public void PasswordRecoverRequest(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum usuário encontrado para esse email"));

//        String token = UUID.randomUUID().toString(); //codigo de verificação grande
        String token = String.format("%05d", new java.util.Random().nextInt(100000)); //codigo de verificação pequeno
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

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
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
