CREATE TABLE subtopic_practice_session (
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            subtopic_id BIGINT NOT NULL REFERENCES subtopics(id),
            current_fen VARCHAR(100),
            status VARCHAR(50),
             moves_played VARCHAR(500),
            created_at TIMESTAMP NOT NULL DEFAULT NOW(),
            updated_at TIMESTAMP
);