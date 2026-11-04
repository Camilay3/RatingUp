ALTER TABLE games
    ADD COLUMN initial_time_seconds    INTEGER     NOT NULL DEFAULT 600,
    ADD COLUMN increment_seconds       INTEGER     NOT NULL DEFAULT 0,
    ADD COLUMN time_control_category   VARCHAR(20) NOT NULL DEFAULT 'RAPID',
    ADD COLUMN result                  VARCHAR(20),
    ADD COLUMN end_reason              VARCHAR(20),
    ADD COLUMN last_move_at            TIMESTAMP,
    ADD COLUMN finished_at             TIMESTAMP,
    ADD COLUMN move_times_ms           TEXT,
    ADD COLUMN white_rating_before     INTEGER,
    ADD COLUMN black_rating_before     INTEGER,
    ADD COLUMN white_rating_change     INTEGER,
    ADD COLUMN black_rating_change     INTEGER,
    ADD COLUMN flagged_for_review      BOOLEAN     NOT NULL DEFAULT FALSE;

CREATE INDEX idx_games_white_player ON games (white_player_id);
CREATE INDEX idx_games_black_player ON games (black_player_id);
CREATE INDEX idx_games_category_finished ON games (time_control_category, finished_at);

CREATE TABLE player_ratings
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            BIGINT      NOT NULL REFERENCES users (id),
    category           VARCHAR(20) NOT NULL,
    rating             INTEGER     NOT NULL DEFAULT 1200,
    games_played       INTEGER     NOT NULL DEFAULT 0,
    wins               INTEGER     NOT NULL DEFAULT 0,
    losses             INTEGER     NOT NULL DEFAULT 0,
    draws              INTEGER     NOT NULL DEFAULT 0,
    performance_rating INTEGER,
    skill_level        VARCHAR(20) NOT NULL DEFAULT 'UNRATED',
    updated_at         TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_player_rating_user_category UNIQUE (user_id, category)
);

CREATE TABLE game_review_flags
(
    id         BIGSERIAL PRIMARY KEY,
    game_id    BIGINT      NOT NULL REFERENCES games (id),
    user_id    BIGINT      NOT NULL REFERENCES users (id),
    score      INTEGER     NOT NULL,
    reasons    TEXT        NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_review_flag_game_user UNIQUE (game_id, user_id)
);

CREATE INDEX idx_review_flags_status ON game_review_flags (status, created_at DESC);