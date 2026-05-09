package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Administrador", description = "Endpoints para gerenciamento dos usuários admins")
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Operation(summary = "Lista todas as contas registradas no banco")
    @GetMapping("/contas/listar")
    public ResponseEntity<ApiResponse<?>> listUsers() {
        List<ProfileResponseDTO> dtos = adminUserService.listUsers()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuários listados", dtos));
    }

    @Operation(summary = "Busca um usuário pelo id")
    @GetMapping("/contas/buscar/{id}")
    public ResponseEntity<ApiResponse<?>> serarchUserById(@PathVariable Long id) {
        User user = adminUserService.searchUserById(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil encontrado", UserMapper.toResponseDTO(user)));
    }

    @Operation(summary = "Cadastra um usuário admin")
    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<?>> registerAdminUser(@Valid @RequestBody ProfileRequestDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setNickname(dto.nickname());
        user.setEmail(dto.email());
        user.setTelefone(dto.telefone());
        user.setPassword(dto.password());
        user.setRole(Roles.ADMIN);

        User newUser = adminUserService.registerAdminUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuário cadastrado com sucesso", UserMapper.toResponseDTO(newUser)));
    }

    @Operation(summary = "Retorna os dados do usuáiro admin")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> showUserAdmin(@AuthenticationPrincipal User adminLogado) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Administrador encontrado", UserMapper.toResponseDTO(adminLogado)));
    }
}
