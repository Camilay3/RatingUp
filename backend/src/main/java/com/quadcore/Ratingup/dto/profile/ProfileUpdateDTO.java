package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.model.profile.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateDTO(
        Long id,
        String nome,
        String nickname,
        @Email(message = "Formato de email inválido!")
        String email,
        String telefone,
        Roles role,
        String senhaAntiga,
        String senhaNova
) {}
