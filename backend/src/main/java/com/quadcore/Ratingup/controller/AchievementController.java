package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.achievement.AchievementResponseDTO;
import com.quadcore.Ratingup.dto.achievement.UserAchievementResponseDTO;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.model.profile.UserAchievement;
import com.quadcore.Ratingup.repository.AchievementRepository;
import com.quadcore.Ratingup.repository.UserAchievementRepository;
import com.quadcore.Ratingup.service.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievement")
public class AchievementController {
    private final AchievementService achievementService;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    public AchievementController(AchievementService achievementService, AchievementRepository achievementRepository, UserAchievementRepository userAchievementRepository) {
        this.achievementService = achievementService;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    @GetMapping
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievements(){
        List<AchievementResponseDTO> list = achievementRepository.findAll()
                .stream()
                .map(a -> new AchievementResponseDTO(a))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserAchievementResponseDTO>> getMyUserAchievement(@AuthenticationPrincipal User user){
        if(user == null){
            return ResponseEntity.status(401).build();
        }
        List<UserAchievementResponseDTO> list = userAchievementRepository.findByUserId(user.getId())
                .stream()
                .map(ua -> new UserAchievementResponseDTO(ua))
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<UserAchievementResponseDTO>> getOtherUserAchievement(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "true") boolean onlyCompleted){
        List<UserAchievementResponseDTO> list;

        if(onlyCompleted) {
            list = userAchievementRepository.findByUserIdAndIsCompletedTrue(userId)
                    .stream()
                    .map(UserAchievementResponseDTO::new)
                    .toList();
        }
        else{
            list = userAchievementRepository.findByUserId(userId)
                    .stream()
                    .map(UserAchievementResponseDTO::new)
                    .toList();
        }

        return ResponseEntity.ok(list);
    }
}
