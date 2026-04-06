package com.quadcore.Ratingup.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequestDTO(
        @NotBlank String nome,
        @NotBlank String nickname,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotBlank @Size(min = 8, max = 12) String senha
) {
}
