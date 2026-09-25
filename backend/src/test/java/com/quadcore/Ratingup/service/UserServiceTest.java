package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.config.security.TokenGenerator;
import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;
import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateRequestDTO;
import com.quadcore.Ratingup.handler.DuplicateFieldException;
import com.quadcore.Ratingup.handler.ValidationException;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private TokenGenerator tokenGenerator;
    
    @Mock
    private ProgressRepository progressRepository;
    
    @Mock
    private EmailService emailService;

    @Mock
    private TokenCookieService tokenCookieService;

    @Mock
    private SecureRandom secureRandom;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("hashed_password");
        user.setNickname("testnick");
        user.setName("Test User");
        user.setTelefone("123456789");
    }

    @Test
    void registerUser_ShouldRegisterSuccessfully() {
        ProfileRequestDTO dto = new ProfileRequestDTO("Test User", "testnick", "test@test.com", "123456789", "Password123!");
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByNickname(anyString())).thenReturn(false);
        when(userRepository.existsByTelefone(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        verify(progressRepository, times(1)).save(any(Progress.class));
    }

    @Test
    void registerUser_ShouldThrowDuplicateFieldException_WhenEmailExists() {
        ProfileRequestDTO dto = new ProfileRequestDTO("Test User", "testnick", "test@test.com", "123456789", "Password123!");
        
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(dto))
                .isInstanceOf(DuplicateFieldException.class)
                .hasMessageContaining("Campos duplicados");
    }

    @Test
    void updateUser_ShouldUpdateSuccessfully() {
        ProfileUpdateRequestDTO dto = new ProfileUpdateRequestDTO("New Name", "newnick", "test@test.com", "987654321");
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User result = userService.updateUser("test@test.com", dto);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getNickname()).isEqualTo("newnick");
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void changePassword_ShouldChangeSuccessfully() {
        PasswordChangeDTO dto = new PasswordChangeDTO("hashed_password", "NewPassword123!");
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("new_hashed_password");

        userService.changePassword("test@test.com", dto);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_ShouldThrowValidationException_WhenOldPasswordIsWrong() {
        PasswordChangeDTO dto = new PasswordChangeDTO("wrong_password", "NewPassword123!");
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword("test@test.com", dto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void loginUser_ShouldReturnToken_WhenCredentialsAreValid() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenGenerator.generateLoginToken(any(User.class))).thenReturn("fake-jwt-token");

        String token = userService.loginUser("test@test.com", "hashed_password");

        assertThat(token).isEqualTo("fake-jwt-token");
    }

    @Test
    void loginUser_ShouldThrowRuntimeException_WhenPasswordIsIncorrect() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.loginUser("test@test.com", "wrong_password"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Senha incorreta");
    }

    @Test
    void passwordRecoverRequest_ShouldGenerateTokenAndSendEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        when(secureRandom.nextInt(100000))
                .thenReturn(12345);

        userService.passwordRecoverRequest("test@test.com");

        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendRecoverMail(eq("test@test.com"), anyString());
        assertThat(user.getResetToken()).isNotNull();
        assertThat(user.getResetTokenExpiry()).isNotNull();
    }

    @Test
    void validateResetToken_ShouldReturnToken_WhenTokenIsValid() {
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10));
        when(userRepository.findByResetToken(anyString())).thenReturn(Optional.of(user));
        when(tokenGenerator.generateRecoveryToken(any(User.class))).thenReturn("fake-jwt-token");

        String token = userService.validateResetToken("valid-token");

        assertThat(token).isEqualTo("fake-jwt-token");
    }

    @Test
    void validateResetToken_ShouldThrowException_WhenTokenIsExpired() {
        user.setResetTokenExpiry(LocalDateTime.now().minusMinutes(10));
        when(userRepository.findByResetToken(anyString())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.validateResetToken("expired-token"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Esse token está expirado");
    }

    @Test
    void deleteUser_ShouldDeleteSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.deleteUser("test@test.com");

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser("notfound@test.com"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void resetPassword_ShouldThrow_WhenPasswordIsWeak() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        String jwt = "recovery-jwt";
        String email = user.getEmail();

        Mockito.when(tokenCookieService.recoverToken(request))
                .thenReturn(jwt);

        Mockito.when(tokenGenerator.getRecoverySubject(jwt))
                .thenReturn(email);

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() ->
                        userService.resetPassword("weak", request)
                )
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("fraca");
    }

    @Test
    void registerUser_ShouldThrow_WhenPasswordHasRepeatedCharacters() {
        com.quadcore.Ratingup.dto.profile.ProfileRequestDTO request = new com.quadcore.Ratingup.dto.profile.ProfileRequestDTO("Name", "Nick", "test@test.com", "11111111", "aaaaaaaaaa1!");
        
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("repetidos");
    }

    @Test
    void resetPassword_ShouldUpdatePassword() {
        when(tokenCookieService.recoverToken(request))
                .thenReturn("recovery-jwt");

        when(tokenGenerator.getRecoverySubject("recovery-jwt"))
                .thenReturn("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("Nova123!Abc"))
                .thenReturn("encoded-password");

        userService.resetPassword("Nova123!Abc", request);

        assertThat(user.getPassword()).isEqualTo("encoded-password");

        verify(passwordEncoder).encode("Nova123!Abc");
        verify(userRepository).save(user);
    }
}
