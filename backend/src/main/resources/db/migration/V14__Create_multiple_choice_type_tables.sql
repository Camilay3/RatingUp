ALTER TABLE subtopics ADD COLUMN type VARCHAR(50);

CREATE TABLE multiple_choice_question (
      id         BIGSERIAL PRIMARY KEY,
      subtopic_id BIGINT NOT NULL UNIQUE,
      question_text TEXT NOT NULL,
      CONSTRAINT fk_mcq_subtopic FOREIGN KEY (subtopic_id) REFERENCES subtopics(id)
);


CREATE TABLE multiple_choice_option (
        id          BIGSERIAL PRIMARY KEY,
        question_id BIGINT NOT NULL,
        option_text VARCHAR(255) NOT NULL,
        is_correct  BOOLEAN NOT NULL DEFAULT FALSE,
        CONSTRAINT fk_mco_question FOREIGN KEY (question_id) REFERENCES multiple_choice_question(id)
);