package com.quadcore.Ratingup.model.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;

@Entity(name = "progresso")
@Table(name = "progresso")
public class Progresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Max(9)
    @Column(nullable = false)
    private Integer capitulo = 1;

    @Column(nullable = false)
    private Integer subtopico = 1;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Integer getCapitulo() {
        return capitulo;
    }

    public Integer getSubtopico() {
        return subtopico;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCapitulo(Integer capitulo) {
        this.capitulo = capitulo;
    }

    public void setSubtopico(Integer subtopico) {
        this.subtopico = subtopico;
    }
}
