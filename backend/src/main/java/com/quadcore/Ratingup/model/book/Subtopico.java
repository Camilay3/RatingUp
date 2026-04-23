package com.quadcore.Ratingup.model.book;

import jakarta.persistence.*;

@Entity
public class Subtopico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer ordem;

    @ManyToOne
    @JoinColumn(name = "capitulo_id")
    private Capitulo capitulo;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public Capitulo getCapitulo() {
        return capitulo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public void setCapitulo(Capitulo capitulo) {
        this.capitulo = capitulo;
    }
}
