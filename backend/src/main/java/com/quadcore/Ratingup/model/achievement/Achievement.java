package com.quadcore.Ratingup.model.achievement;

import com.quadcore.Ratingup.enums.AchievementCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "achievements")
@Table(name = "achievements", uniqueConstraints = {
    @UniqueConstraint(name = "uk_achievement_code", columnNames = {"code"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Código é obrigatório.")
    @Size(min = 5, max = 50)
    private String code;

    @NotBlank(message = "Título é obrigatório.")
    @Size(min = 10, max = 100)
    private String title;

    @NotBlank(message = "Descrição é obrigatória.")
    @Size(min = 30, max = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementCategory category;

    @NotNull(message = "Valor de meta é obrigatório.")
    @Column(nullable = false)
    private int targetValue;

    @NotNull(message = "XP de recompensa é obrigatório.")
    @Column(nullable = false)
    private int rewardExperience;

    @Size(max = 50)
    private String rewardTitle;
}
