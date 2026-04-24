package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;
import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.UserRepository;
import com.quadcore.Ratingup.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
        user.setRole(dto.role());

        User newUser = userService.criarUsuario(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuário cadastrado com sucesso", UserMapper.toResponseDTO(newUser)));
    }

    /**
     * Realiza o login do usuário por meio do email e da senha
     *
     * @param dto JSON com os dados necessários para o login (email e senha)
     * @return retorna o token do login, caso seja bem sucedido
     * */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> logarUsuario(@RequestBody ProfileRequestDTO dto){
        String token = userService.loginUsuario(dto.email(),dto.senha());

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário realizou login com sucesso", token));
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
    @PatchMapping("meu-perfil/{id}/atualizar")
    public ResponseEntity<ApiResponse<?>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody ProfileUpdateDTO dto) {
        User data = new User();
        data.setNome(dto.nome());
        data.setNickname(dto.nickname());
        data.setEmail(dto.email());
        data.setTelefone(dto.telefone());
        data.setRole(dto.role());
        data.setTelefone(dto.telefone());

        PasswordChangeDTO passwordDto = new PasswordChangeDTO(dto.senhaAntiga(), dto.senhaNova());
        Optional<User> userAtualizado = userService.atualizarUsuario(id, data, passwordDto);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado", userAtualizado.map(UserMapper::toResponseDTO)));

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
