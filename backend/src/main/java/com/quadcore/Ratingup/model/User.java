package com.quadcore.Ratingup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "users")
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uk_user_nickname", columnNames = {"nickname"}),
                @UniqueConstraint(name = "uk_user_tel", columnNames = {"telefone"})
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo obrigatório!")
    private String nome;

    @NotBlank(message = "Nickname obrigatório!")
    @Size(min = 8, max = 16)
    private String nickname;

    @NotBlank(message = "Email obrigatório!")
    @Email(message = "Email em formato inválido!")
    private String email;

    @NotBlank(message = "Telefone obrigatório!")
    @Size(min = 8, max = 11)
    private String telefone;

    @NotBlank(message = "Senha obrigatória!")
    @Size(min = 8, max = 12)
    private String senha;

    private String role;

    @ColumnDefault("1")
    @Column(nullable = false)
    private Integer faseAtual = 1;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenha() {
        return senha;
    }

    public String getRole() {
        return role;
    }

    public Integer getFaseAtual() {
        return faseAtual;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFaseAtual(Integer faseAtual) {
        this.faseAtual = faseAtual;
    }
}
