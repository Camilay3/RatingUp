CREATE TABLE achievements (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    category VARCHAR(30) NOT NULL,
    target_value INT NOT NULL,
    reward_experience INT NOT NULL,
    reward_title VARCHAR(50),
    CONSTRAINT uk_achievement_code UNIQUE (code)
);

CREATE TABLE user_achievements (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL,
    current_progress INT NOT NULL DEFAULT 0,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (achievement_id) REFERENCES achievements(id) ON DELETE CASCADE,
    CONSTRAINT unique_key_user_achievement UNIQUE (user_id, achievement_id)
);

INSERT INTO achievements (code, title, description, category, target_value, reward_experience, reward_title)
VALUES
    ('FIRST_MATCH', 'Primeira Partida', 'Jogue uma partida pela primeira vez', 'CONSISTENCY', 1, 50, NULL),
    ('FIRST_MULTIPLAYER_VICTORY', 'Começo triunfal', 'Vença uma partida multiplayer de xadrez', 'CONSISTENCY', 1, 100, 'Iniciante Promissor'),

    ('COMPLETE_10_LESSONS', 'Enxadrista Focado', 'Conclua 10 lições teóricas do livro', 'THEORETICAL', 10, 200, 'Teórico'),
    ('TACTICAL_FORK', 'Ataque Duplo', 'Execute um garfo com sucesso', 'TACTICAL', 1, 150, 'Tático');