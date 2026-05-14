package com.quadcore.Ratingup.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(example = """
            {
                "email": "ratingupadmin@gmail.com",
                "password": "Ab@12345"
            }
            """)
public record LoginRequestDTO(

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {}
