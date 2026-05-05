CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefone VARCHAR(11) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,

    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT uk_user_nickname UNIQUE (nickname),
    CONSTRAINT uk_user_tel UNIQUE (telefone)
);

CREATE TABLE chapters (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    display_order INTEGER NOT NULL
);

CREATE TABLE progress (
    id BIGSERIAL PRIMARY KEY,
    chapters INTEGER NOT NULL DEFAULT 1,
    subtopics INTEGER NOT NULL DEFAULT 1,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id)
);

CREATE TABLE subtopics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    display_order INTEGER NOT NULL,
    chapter_id BIGINT NOT NULL REFERENCES chapters(id)
);