package com.quadcore.Ratingup.service;


import com.quadcore.Ratingup.event.AchievementUnlockedEvent;
import com.quadcore.Ratingup.model.achievement.Achievement;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.model.profile.UserAchievement;
import com.quadcore.Ratingup.repository.AchievementRepository;
import com.quadcore.Ratingup.repository.UserAchievementRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

@Service
public class AchievementService {
    private final UserRepository userRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AchievementService(UserRepository userRepository, UserAchievementRepository userAchievementRepository, AchievementRepository achievementRepository, ApplicationEventPublisher applicationEventPublisher){
        this.userRepository = userRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.achievementRepository = achievementRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public UserAchievement updateUserAchievement(Long userId, String achievementCode, int incrementProgress){
        Achievement achievement = achievementRepository.findByCode(achievementCode)
                .orElseThrow(() -> new RuntimeException("Conquista não encontrada."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> {
                    UserAchievement newAchievement = new UserAchievement();
                    newAchievement.setUser(user);
                    newAchievement.setAchievement(achievement);
                    newAchievement.setCurrentProgress(0);
                    newAchievement.setCompleted(false);
                    return newAchievement;
                });

        if (userAchievement.isCompleted()){
            return userAchievement;
        }

        userAchievement.setCurrentProgress(userAchievement.getCurrentProgress() + incrementProgress);

        if (userAchievement.getCurrentProgress() >= achievement.getTargetValue()){
            userAchievement.setCompleted(true);
            userAchievement.setCompletedAt(LocalDateTime.now());

            user.setExperience(user.getExperience() + achievement.getRewardExperience());
            userRepository.save(user);

            applicationEventPublisher.publishEvent(new AchievementUnlockedEvent(user, achievement));
        }
        return userAchievementRepository.save(userAchievement);
    }
}
