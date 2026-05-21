package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.NotBlank;

public record AvatarSelectionDTO(
        @NotBlank(message = "O nome da imagem não pode ser vazio")
        String imageName
) {
}
