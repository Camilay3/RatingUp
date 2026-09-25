package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.config.security.TokenGenerator;
import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;

import com.quadcore.Ratingup.dto.profile.PasswordResetDTO;
import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateRequestDTO;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.exception.*;

import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.security.SecureRandom;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final EmailService emailService;
    private final ConcurrentHashMap<String, Long> rateLimitMap = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom;
    private final TokenCookieService tokenCookieService;

    public UserService(UserRepository userRepository, ProgressRepository progressRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator, EmailService emailService, SecureRandom secureRandom, TokenCookieService tokenCookieService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.progressRepository = progressRepository;
        this.emailService = emailService;
        this.secureRandom = secureRandom;
        this.tokenCookieService = tokenCookieService;
    }

    public boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        long window = 60000; // 1 minute
        rateLimitMap.values().removeIf(time -> now - time > window);
        long attempts = rateLimitMap.entrySet().stream().filter(e -> e.getKey().startsWith(ip + "_")).count();
        if (attempts >= 5) {
            return true;
        }
        rateLimitMap.put(ip + "_" + now + "_" + UUID.randomUUID(), now);
        return false;
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
            throw new ConflictException("Campos duplicados", errors);
        }

        User user = UserMapper.toEntity(dto);
        user.setRole(Roles.USER);

        checkRepeatedCharactersPassword(user.getPassword());

        String senhaCriptografada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCriptografada);

        //talvez fazer uma função so pra isso aq(mateus)
        User registeredUser = saveUser(user);


        return registeredUser;
    }

    public User saveUser(User user){
        User savedUser = userRepository.save(user);
        LocalDateTime creationDate = savedUser.getCreationDate();
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

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new FieldValidationException(
                    "oldPassword",
                    "Senha antiga não está correta"
            );
        }

        if (dto.oldPassword().equals(dto.newPassword())) {
            throw new FieldValidationException(
                    "newPassword",
                    "A nova senha não pode ser igual à antiga"
            );
        }
        checkRepeatedCharactersPassword(dto.newPassword());
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    public String loginUser(String email, String senha){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado para esse email"));

        if(!(passwordEncoder.matches(senha, user.getPassword()))) {
            throw new UnauthorizedOperationException("Senha incorreta");
        }

        return tokenGenerator.generateLoginToken(user);
    }

    public void passwordRecoverRequest(String email){
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return;
        }
        User user = userOpt.get();

        String token = String.format("%05d", secureRandom.nextInt(100000));
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        emailService.sendRecoverMail(user.getEmail(), token);
    }

    @Transactional
    public String validateResetToken(String token){
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new UnauthorizedOperationException("Token inválido"));

        if(user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedOperationException("Esse token está expirado");
        }

        String jwt = tokenGenerator.generateRecoveryToken(user);

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);

        return jwt;
    }

    public void resetPassword(String newPassword, HttpServletRequest request){
        String jwt = tokenCookieService.recoverToken(request);

        String email = tokenGenerator.getRecoverySubject(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";
        if(!newPassword.matches(regex)){
            throw new FieldValidationException("newPassword", "Senha nova fraca! Digite uma senha que tenha letras maiúsculas, minúsculas, números e símbolos");
        }

        checkRepeatedCharactersPassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void checkRepeatedCharactersPassword(String senha){
        int contadorSenha = 0;
        for(int i = 0;i <= senha.length()-2; i++){
            if(senha.charAt(i) == senha.charAt(i+1)){
                contadorSenha++;

                if(contadorSenha >= 7){
                    throw new FieldValidationException("newPassword", "Erro: A senha está com 8 ou mais caracteres repetidos/consecutivos!");
                }
            }
            else{
                contadorSenha = 0;
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

    }
}
