package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/contas")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<?>> listarUsuarios() {
        List<ProfileResponseDTO> dtos = adminUserService.listarUsuarios()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuários listados", dtos));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse<?>> buscarUsuario(@PathVariable Long id) {
        User user = adminUserService.buscarUsuario(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil encontrado", UserMapper.toResponseDTO(user)));
    }
}
