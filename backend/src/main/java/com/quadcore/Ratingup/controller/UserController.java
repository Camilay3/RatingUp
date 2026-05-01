package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.*;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.UserRepository;
import com.quadcore.Ratingup.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<?>> logarUsuario(@RequestBody LoginRequestDTO dto){
        String token = userService.loginUsuario(dto.email(),dto.senha());

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário realizou login com sucesso", token));
    }


    @PatchMapping("meu-perfil/atualizar") //tirando o id da url,pq se n qlqr usuario pode atualizar o perfl de outro ao mudar o id
    public ResponseEntity<ApiResponse<?>> atualizarUsuario(@AuthenticationPrincipal User logado, @Valid @RequestBody ProfileUpdateDTO dto) {
        User data = new User();
        data.setNome(dto.nome());
        data.setNickname(dto.nickname());
        data.setEmail(dto.email()); //Vou colocar esses campos em outro controller no meu próximo pr (Kalebe)
        data.setTelefone(dto.telefone());
        data.setRole(dto.role());
        data.setTelefone(dto.telefone());
//        data.setSenha(dto.senhaNova()); //Vou colocar esses campos em outro controller no meu próximo pr (Kalebe) //comentei pq a linha logo a baixo ja cuida da senha

        PasswordChangeDTO passwordDto = new PasswordChangeDTO(dto.senhaAntiga(), dto.senhaNova());
        Optional<User> userAtualizado = userService.atualizarUsuario(logado.getId(), data, passwordDto);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado", userAtualizado.map(UserMapper::toResponseDTO)));

    }


    @DeleteMapping("meu-perfil/deletar")
    public ResponseEntity<ApiResponse<?>> deletarUsuario(@AuthenticationPrincipal User logado) {
        userService.deletarUsuario(logado.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuário deletado com sucesso", null));
    }


}
