package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.*;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.enums.Roles;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Cadastra um usuário")
    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody ProfileRequestDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setNickname(dto.nickname());
        user.setEmail(dto.email());
        user.setTelefone(dto.telefone());
        user.setPassword(dto.password());
        user.setRole(Roles.USER);
        User newUser = userService.registerUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuário cadastrado com sucesso", UserMapper.toResponseDTO(newUser)));
    }

    @PostMapping("/login")
	public ResponseEntity<ApiResponse<?>> loginUser(
		@Valid @RequestBody LoginRequestDTO dto,
		HttpServletResponse response
	) {

		String token = userService.loginUser(
				dto.email(),
				dto.password()
		);

		ResponseCookie cookie = ResponseCookie
				.from("token", token)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.sameSite("Lax")
				.maxAge(Duration.ofDays(7))
				.build();

		response.addHeader(
				HttpHeaders.SET_COOKIE,
				cookie.toString()
		);

		return ResponseEntity.ok(
				new ApiResponse<>(
						true,
						"Usuário realizou login com sucesso",
						null
				)
		);
	}


    @Operation(summary = "Atualiza parcialmente os dados do usuário")
    @PatchMapping("me/atualizar")
    public ResponseEntity<ApiResponse<?>> updateUser(@AuthenticationPrincipal User logado, @Valid @RequestBody ProfileUpdateDTO dto) {
        Optional<User> userAtualizado = userService.updateUser(logado.getEmail(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado", userAtualizado.map(UserMapper::toResponseDTO)));
    }


    @Operation(summary = "Deleta o usuário")
    @DeleteMapping("me/deletar")
    public ResponseEntity<ApiResponse<?>> deleteUser(@AuthenticationPrincipal User logado) {
        userService.deleteUser(logado.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário deletado com sucesso", null));
    }

    @Operation(summary = "Retorna os dados do usuário")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> showUser(@AuthenticationPrincipal User logado) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário encontrado", UserMapper.toResponseDTO(logado)));
    }

}
