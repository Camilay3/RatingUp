package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateDTO;
import com.quadcore.Ratingup.model.profile.User;

public abstract class UserMapper {

    public static User toEntity(ProfileRequestDTO dto) {
        User usuario = new User();

        usuario.setName(dto.name());
        usuario.setNickname(dto.nickname());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        usuario.setPassword(dto.password());

        return usuario;
    }

    public static ProfileRequestDTO toRequestDTO(User user) {
        return new ProfileRequestDTO(user);
    }

    public static ProfileResponseDTO toResponseDTO(User user) {
        return new ProfileResponseDTO(user);
    }

    public static ProfileUpdateDTO toUpdateDTO(User user) {
        return new ProfileUpdateDTO(user);
    }
}
