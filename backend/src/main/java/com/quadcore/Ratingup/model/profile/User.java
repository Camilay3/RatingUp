package com.quadcore.Ratingup.model.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "users")
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uk_user_nickname", columnNames = {"nickname"}),
                @UniqueConstraint(name = "uk_user_tel", columnNames = {"telefone"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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
    private String senha;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private String role;

    @OneToOne(mappedBy = "user")
    private Progresso progresso;

}
