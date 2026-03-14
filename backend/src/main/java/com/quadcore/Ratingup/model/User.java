package com.quadcore.Ratingup.model;

public class User {

    private Long id;
    private String nome;
    private String email;
    private int rating;

    public User(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.rating = 1200;
    }
}
