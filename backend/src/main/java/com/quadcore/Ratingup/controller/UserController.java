package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.*;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import java.time.Duration;

/***
* API Rest responsável pela comunicação do cadastro de usuários
*
* @author Equipe Quadcore
* @version 1.0
* @since 2026-04-05
* */

@Tag(name = "Usuário")
@RestController
@RequestMapping("/conta")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Cadastra um novo usuário",description = "cadastra um novo usuário no sitema com a role user")
    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody ProfileRequestDTO dto) {
        User user = UserMapper.toEntity(dto);
        user.setRole(Roles.USER);
        User newUser = userService.registerUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuário cadastrado com sucesso", UserMapper.toResponseDTO(newUser)));
    }

    @Operation(summary = "Atualiza parcialmente os dados do usuário",description = "atualiza dados de usuário e devolve um dto com as novas informações atualizadas do usuário")
    @PatchMapping("me/atualizar")
    public ResponseEntity<ApiResponse<?>> updateUser(@AuthenticationPrincipal User loggedUser, @Valid @RequestBody ProfileUpdateRequestDTO dto) {
        Optional<User> userAtualizado = userService.updateUser(loggedUser.getEmail(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado", userAtualizado.map(UserMapper::toResponseDTO)));
    }


    @Operation(summary = "Deleta o usuário",description = "faz um hard delete no sistema de um usuário")
    @DeleteMapping("me/deletar")
    public ResponseEntity<ApiResponse<?>> deleteUser(@AuthenticationPrincipal User loggedUser) {
        userService.deleteUser(loggedUser.getEmail());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Usuário deletado com sucesso", null));
    }

    @Operation(summary = "Retorna os dados do usuário",description = "devolve um dto completo com dados do usuário")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> showUser(@AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário encontrado", UserMapper.toResponseDTO(loggedUser)));
    }

}
