-- =============================================
-- Capítulo 1 — Introdução ao Xadrez
-- =============================================

-- O que é o xadrez
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- Objetivo do jogo
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- História do xadrez
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- Como o xadrez se tornou esporte
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- O que é Rating
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- Vocabulário básico do xadrez
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- Vocabulário popular do xadrez
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))), 'eee', false);

-- =============================================
-- Capítulo 2 — O Tabuleiro e as Peças
-- =============================================

-- Estrutura do tabuleiro
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'eee', false);

-- Casas claras e escuras
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'eee', false);

-- Colunas, fileiras e diagonais
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'eee', false);

-- Nomeação das casas
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'eee', false);

-- Conhecendo as peças
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'eee', false);

-- Valor relativo das peças
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))), 'eee', false);


-- =============================================
-- Capítulo 4 — Regras Fundamentais
-- =============================================

-- Tempo
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))), 'eee', false);


-- =============================================
-- Capítulo 6 — Princípios Básicos de Abertura
-- =============================================

-- Não mover a mesma peça várias vezes
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Não mover a mesma peça várias vezes' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'eee', false);


-- Padronização de aberturas
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))), 'eee', false);

-- =============================================
-- Capítulo 7 — Noções Básicas de Tática
-- =============================================

-- Estrutura de peões
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'eee', false);

-- CCT
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))), 'eee', false);

-- =============================================
-- Capítulo 8 — Como desenvolver o meio-jogo
-- =============================================

-- O que é o meio-jogo
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'eee', false);

-- Planos no meio-jogo
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'eee', false);

-- Ataque ao rei
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'eee', false);

-- Peças boas vs peças ruins
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Peças boas vs peças ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Peças boas vs peças ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Peças boas vs peças ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Peças boas vs peças ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Peças boas vs peças ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Peças boas vs peças ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))), 'eee', false);


-- =============================================
-- Capítulo 9 — Formas de ganhar o final de um jogo
-- =============================================

-- O que é o final
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'eee', false);

-- Tipos de final
INSERT INTO multiple_choice_question (subtopic_id, question_text)
VALUES ((SELECT id FROM subtopics WHERE title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9)), 'Qual o item certo?');

INSERT INTO multiple_choice_option (question_id, option_text, is_correct) VALUES
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'aaa', true),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'bbb', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'ccc', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'ddd', false),
                                                                              ((SELECT id FROM multiple_choice_question WHERE subtopic_id = (SELECT id FROM subtopics WHERE title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))), 'eee', false);
