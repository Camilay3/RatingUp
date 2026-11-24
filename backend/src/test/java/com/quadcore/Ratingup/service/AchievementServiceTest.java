package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.event.AchievementUnlockedEvent;
import com.quadcore.Ratingup.model.achievement.Achievement;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.model.profile.UserAchievement;
import com.quadcore.Ratingup.repository.AchievementRepository;
import com.quadcore.Ratingup.repository.UserAchievementRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private AchievementService achievementService;

    private User user;
    private Achievement achievement;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setExperience(0);

        achievement = new Achievement();
        achievement.setId(10L);
        achievement.setCode("FIRST_MATCH");
        achievement.setTitle("Primeira Partida");
        achievement.setTargetValue(1);
        achievement.setRewardExperience(50);
    }

    @Test
    @DisplayName("O objetivo é desbloquear a conquista, dar XP ao usuário e emitir evento caso alcance a meta da conquista")
    void deveDesbloquearConquistaComSucesso(){
        when(achievementRepository.findByCode("FIRST_MATCH")).thenReturn(Optional.of(achievement));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userAchievementRepository.findByUserIdAndAchievementId(1L, 10L)).thenReturn(Optional.empty());
        when(userAchievementRepository.save(any(UserAchievement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAchievement result = achievementService.updateUserAchievement(1L, "FIRST_MATCH", 1);

        assertTrue(result.isCompleted(), "A conquista deveria estar marcada como completa");
        assertNotNull(result.getCompletedAt(), "A data de conclusão não deve ser nula");
        assertEquals(50, user.getExperience(), "O usuário deveria ter recebido 50 de experiência(XP)");

        verify(userRepository, times(1)).save(user);
        verify(applicationEventPublisher, times(1)).publishEvent(any(AchievementUnlockedEvent.class));
    }
}
