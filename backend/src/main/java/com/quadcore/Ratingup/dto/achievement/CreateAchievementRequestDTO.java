package com.quadcore.Ratingup.dto.achievement;

import com.quadcore.Ratingup.enums.AchievementCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateAchievementRequestDTO(
        @NotBlank(message = "Código é obrigatório")
        @Size(min = 5, max = 50)
        String code,

        @NotBlank(message = "Título é obrigatório")
        @Size(min = 10, max = 100)
        String title,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 30, max = 200)
        String description,

        @NotNull(message = "Categoria é obrigatória")
        AchievementCategory category,

        @NotNull(message = "Valor de meta é obrigatório")
        @Positive
        Integer targetValue,

        @NotNull(message = "XP é obrigatório")
        @Positive
        Integer rewardExperience,

        @Size(max = 50)
        String rewardTitle
) {
}
