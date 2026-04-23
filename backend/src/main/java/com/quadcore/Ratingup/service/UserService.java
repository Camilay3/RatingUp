package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.config.security.TokenGenerator;
import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;

import com.quadcore.Ratingup.model.profile.Progresso;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ProgressoRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final ProgressoRepository progressoRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProgressoRepository progressoRepository, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.progressoRepository = progressoRepository;
    }

    @Transient
    public User criarUsuario(User user) {
        String senhaPadrao = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";

        if(user.getSenha() == null || user.getSenha().isEmpty()){
            throw new RuntimeException("A senha não pode ser nula");
        }
        if(!user.getSenha().matches(senhaPadrao)){
            throw new RuntimeException("Senha inválida! A senha precisa ter entre 8 a 12 caracteres, que tenham letras maiúsculas, minúsculas, números e símbolos");
        }

        String senhaCriptografada = passwordEncoder.encode(user.getSenha());
        user.setSenha(senhaCriptografada);

        User savedUser = userRepository.save(user);
        Progresso progresso = new Progresso();
        progresso.setUser(savedUser);
        savedUser.setProgresso(progresso);
        progressoRepository.save(progresso);

        return savedUser;
    }

    public User buscarPorID(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Optional<User> atualizarUsuario(Long id, User data, PasswordChangeDTO dto) {
        Optional<User> optionalUser = Optional.of(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        User user = optionalUser.get();

        String senhaAntiga = user.getSenha();

        if (data.getNome() != null) {
            user.setNome(data.getNome());
        }
        if (data.getNickname() != null) {
            user.setNickname(data.getNickname());
        }
        if (data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }
        if (data.getTelefone() != null) {
            user.setTelefone(data.getTelefone());
        }


        if(dto.senhaNova() != null && !dto.senhaNova().isEmpty()){
            this.alterarSenha(user,dto);
        }
        else {
            throw new RuntimeException("Erro: A senha foi recebida como null");
        }

        userRepository.save(user);

        return Optional.of(user);
    }

    public Optional<User> deletarUsuario(Long id) {
        Optional<User> user = Optional.of(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        user.ifPresent(userRepository::delete);

        return user;
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    private void alterarSenha(User user, PasswordChangeDTO dto){

        if(!passwordEncoder.matches(dto.senhaAntiga(), user.getSenha())){
            throw new RuntimeException("As senhas não combinam");
        }

        String senhaPadrao = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!¨])(?=\\S+$).{8,12}$";
        if(!dto.senhaNova().matches(senhaPadrao)){
            throw new RuntimeException("Senha nova fraca! Digite uma senha que tenha letras maiúsculas, minúsculas, números e símbolos");
        }
        user.setSenha(passwordEncoder.encode(dto.senhaNova()));
    }

    public String loginUsuario(String email, String senha){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(passwordEncoder.matches(senha, user.getSenha())){
            return tokenGenerator.gerarToken(user);
        }

        throw new RuntimeException("Senha inválida");
    }
}
