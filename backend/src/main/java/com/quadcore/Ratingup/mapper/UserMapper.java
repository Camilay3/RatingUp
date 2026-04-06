package com.quadcore.Ratingup.mapper;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.model.User;

public class UserMapper {

    private UserMapper() {
    }

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
        ProfileRequestDTO dto = new ProfileRequestDTO(
                user.getNome(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getSenha()
        );
        return dto;
    }

    public static ProfileResponseDTO toResponseDTO(User user) {
        ProfileResponseDTO dto = new ProfileResponseDTO(
                user.getId(),
                user.getNome(),
                user.getNickname(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole()
        );
        return dto;
    }
}
