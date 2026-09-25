package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.profile.AvatarResponseDTO;
import com.quadcore.Ratingup.model.images.Images;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ImagesRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvatarServiceTest {

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AvatarService avatarService;

    private User user;
    private Images image;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com");

        image = new Images();
        image.setImageName("avatar1.png");
        image.setBucketName("avatars");
        image.setObjectId("obj-123");
    }

    @Test
    void listAvaliableImages_ShouldReturnList() {
        when(imagesRepository.findAllByBucketName("avatars")).thenReturn(List.of(image));

        List<AvatarResponseDTO> result = avatarService.listAvaliableImages();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).imageName()).isEqualTo("avatar1.png");
    }

    @Test
    void selectAvatar_ShouldSetAvatarSuccessfully() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(imagesRepository.findByImageName("avatar1.png")).thenReturn(Optional.of(image));

        avatarService.selectAvatar("avatar1.png");

        assertThat(user.getAvatarurl()).isEqualTo("/images/avatars/obj-123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void selectAvatar_ShouldThrowException_WhenUserNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avatarService.selectAvatar("avatar1.png"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    void selectAvatar_ShouldThrowException_WhenImageNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(imagesRepository.findByImageName("avatar1.png")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avatarService.selectAvatar("avatar1.png"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Imagem não encontrada");
    }
}
