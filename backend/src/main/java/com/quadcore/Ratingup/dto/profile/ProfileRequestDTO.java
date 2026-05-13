package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.model.profile.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ProfileRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Nickname é obrigatório")
        @Size(min= 8, max =12, message = "o nickname deve ter entre 8 a 16 caracteres")
        String nickname,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{8,11}", message = "Telefone deve conter apenas números, entre 8 e 11 dígitos")
        @Schema(example = "telefone")
        String telefone,

        @NotBlank(message = "Senha é obrigatória")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$",
                message = "A senha precisa ter entre 8 a 12 caracteres, com maiúsculas, minúsculas, números e símbolos")
        @Schema(example = "SenhaExemplo@123")
        String password
){
    public ProfileRequestDTO(User user) {
        this(
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getPassword()
        );
    }
}
