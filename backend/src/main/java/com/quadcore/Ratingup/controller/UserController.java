package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.UserDTO;
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

    private UserDTO conversaoDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @PostMapping
    public ResponseEntity<UserDTO> criarUsuario(@Valid @RequestBody UserDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());

        return userService.criarUsuario(user)
                .map(u -> ResponseEntity.ok(conversaoDTO(u)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarPorID(@PathVariable Long id) {
        Optional<User> user = userService.buscarPorID(id);

        return user
                .map(value -> ResponseEntity.ok(conversaoDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserDTO> listarUsuarios() {
        List<User> usuarios = userService.listarUsuarios();

        List<UserDTO> dtos = usuarios.stream()
                .map(this::conversaoDTO)
                .collect(Collectors.toList());

        return dtos;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        User data = new User();
        data.setNome(dto.getNome());
        data.setEmail(dto.getEmail());

        Optional<User> userAtualizado = userService.atualizarUsuario(id, data);

        return userAtualizado
                .map(user -> ResponseEntity.ok(conversaoDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        return userService.deletarUsuario(id)
                .map(user -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
