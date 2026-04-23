package com.quadcore.Ratingup.model.book;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer ordem;

    @OneToMany(mappedBy = "capitulo", fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<Subtopico> subtopicos;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public List<Subtopico> getSubtopicos() {
        return subtopicos;
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

    public void setSubtopicos(List<Subtopico> subtopicos) {
        this.subtopicos = subtopicos;
    }
}
