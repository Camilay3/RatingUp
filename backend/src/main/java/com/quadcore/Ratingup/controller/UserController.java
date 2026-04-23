package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/***
* API Rest responsável pela comunicação do cadastro de usuários
*
* @author Equipe Quadcore
* @version 1.0
* @since 2026-04-05
* */

@RestController
@RequestMapping("/conta")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
    * Cria um usuário
    *
    * @param dto JSON com os dados do usuário
    * @return retorna um usuário criado
    * */
    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<?>> criarUsuario(@Valid @RequestBody ProfileRequestDTO dto) {
        User user = new User();
        user.setNome(dto.nome());
        user.setNickname(dto.nickname());
        user.setEmail(dto.email());
        user.setTelefone(dto.telefone());
        user.setSenha(dto.senha());

        User newUser = userService.criarUsuario(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuário cadastrado com sucesso", UserMapper.toResponseDTO(newUser)));
    }

    /**
     * Busca um usuário pelo id
     *
     * @param id id do usuário
     * @return retorna o usuário correspondente ao id fornecido
     * */
    @GetMapping("/buscar-usuario/{id}")
    public ResponseEntity<ApiResponse<?>> buscarPorID(@PathVariable Long id) {
        User user = userService.buscarPorID(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil encontrado", UserMapper.toResponseDTO(user)));
    }

    /**
     * Lista todos os usuários criados
     *
     * @return retorna uma lista dos usuários criados
     * */
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<?>> listarUsuarios() {
        List<ProfileResponseDTO> dtos = userService.listarUsuarios()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário listados", dtos));
    }

    /**
     * Atualiza o usuário pelo id
     *
     * @param id id do usuário
     * @param dto JSON com os dados atualizados do usuário
     * @return retorna o usuário atualizado
     * */
    @PutMapping("meu-perfil/{id}/atualizar")
    public ResponseEntity<ApiResponse<?>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody ProfileUpdateDTO dto) {
        User data = new User();
        data.setNome(dto.nome());
        data.setNickname(dto.nickname());
        data.setEmail(dto.email());
        data.setTelefone(dto.telefone());

        Optional<User> userAtualizado = userService.atualizarUsuario(id, data);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado", userAtualizado));

    }

    /**
     * Deleta o usuário pelo id
     *
     * @param id id do usuário
     * */
    @DeleteMapping("meu-perfil/{id}/deletar")
    public ResponseEntity<ApiResponse<?>> deletarUsuario(@PathVariable Long id) {
        userService.deletarUsuario(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário deletado com sucesso", null));
    }
}
