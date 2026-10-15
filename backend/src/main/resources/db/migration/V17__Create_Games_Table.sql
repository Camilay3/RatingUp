CREATE TABLE games
(
    id              BIGSERIAL PRIMARY KEY,
    white_player_id BIGINT       NOT NULL REFERENCES users (id),
    black_player_id BIGINT REFERENCES users (id),
    current_fen     VARCHAR(100) NOT NULL,
    moves_played    TEXT,
    move_number     INTEGER      NOT NULL DEFAULT 0,
    status          VARCHAR(30)  NOT NULL,
    version         BIGINT       NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP
)