package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateDTO;
import com.quadcore.Ratingup.model.profile.User;

public abstract class UserMapper {

    public static User toEntity(ProfileRequestDTO dto) {
        User usuario = new User();

        usuario.setNome(dto.nome());
        usuario.setNickname(dto.nickname());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        usuario.setSenha(dto.senha());

        return usuario;
    }

    public static ProfileRequestDTO toRequestDTO(User user) {
        ProfileRequestDTO dto = new ProfileRequestDTO(user);
        return dto;
    }

    public static ProfileResponseDTO toResponseDTO(User user) {
        ProfileResponseDTO dto = new ProfileResponseDTO(user);
        return dto;
    }
}
