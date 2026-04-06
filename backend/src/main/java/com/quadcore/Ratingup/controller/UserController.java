package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateDTO;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ProfileRequestDTO> criarUsuario(@Valid @RequestBody ProfileRequestDTO dto) {
        User user = new User();
        user.setNome(dto.nome());
        user.setNickname(dto.nickname());
        user.setEmail(dto.email());
        user.setTelefone(dto.telefone());
        user.setSenha(dto.senha());

        return userService.criarUsuario(user)
                .map(u -> ResponseEntity.ok(UserMapper.toRequestDTO(u)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> buscarPorID(@PathVariable Long id) {
        Optional<User> user = userService.buscarPorID(id);

        return user
                .map(value -> ResponseEntity.ok(UserMapper.toResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<ProfileResponseDTO> listarUsuarios() {
        List<User> usuarios = userService.listarUsuarios();

        List<ProfileResponseDTO> dtos = usuarios.stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());

        return dtos;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody ProfileUpdateDTO dto) {
        User data = new User();
        data.setNome(dto.nome());
        data.setNickname(dto.nickname());
        data.setEmail(dto.email());
        data.setTelefone(dto.telefone());

        Optional<User> userAtualizado = userService.atualizarUsuario(id, data);

        return userAtualizado
                .map(user -> ResponseEntity.ok(UserMapper.toResponseDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        return userService.deletarUsuario(id)
                .map(user -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
