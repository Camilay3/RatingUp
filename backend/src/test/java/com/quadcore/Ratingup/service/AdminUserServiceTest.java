package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserService adminUserService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("admin@test.com");
        user.setPassword("Pass123!@#");
        user.setRole(Roles.ADMIN);
    }

    @Test
    void listUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<ProfileResponseDTO> result = adminUserService.listUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("admin@test.com");
    }

    @Test
    void searchUserById_ShouldReturnUser_WhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = adminUserService.searchUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void searchUserById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.searchUserById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    void registerAdminUser_ShouldRegisterSuccessfully() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = adminUserService.registerAdminUser(user);

        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(Roles.ADMIN);
        verify(progressRepository, times(1)).save(any(Progress.class));
    }

    @Test
    void registerAdminUser_ShouldThrowException_WhenPasswordIsNull() {
        user.setPassword(null);

        assertThatThrownBy(() -> adminUserService.registerAdminUser(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("A senha não pode ser nula");
    }

    @Test
    void registerAdminUser_ShouldThrowException_WhenPasswordIsInvalid() {
        user.setPassword("weak");

        assertThatThrownBy(() -> adminUserService.registerAdminUser(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Senha inválida!");
    }
}
