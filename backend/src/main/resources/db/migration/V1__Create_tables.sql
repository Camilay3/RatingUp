CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefone VARCHAR(11) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(255),

    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT uk_user_nickname UNIQUE (nickname),
    CONSTRAINT uk_user_tel UNIQUE (telefone)
);

CREATE TABLE capitulo (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    ordem INTEGER NOT NULL
);

CREATE TABLE progresso (
    id BIGSERIAL PRIMARY KEY,
    capitulo INTEGER NOT NULL DEFAULT 1,
    subtopico INTEGER NOT NULL DEFAULT 1,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id)
);

CREATE TABLE subtopico (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    ordem INTEGER NOT NULL,
    capitulo_id BIGINT NOT NULL REFERENCES capitulo(id)
);