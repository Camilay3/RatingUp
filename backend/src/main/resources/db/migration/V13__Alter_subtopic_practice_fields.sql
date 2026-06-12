-- =============================================
-- Capítulo 3 — Movimento das Peças
-- =============================================

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/4P3/8/8/8 w - - 0 1',
                     solution_moves = 'e4e5'
WHERE title = 'Movimento do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/8/8/8/R7 w - - 0 1',
                     solution_moves = 'a1h1'
WHERE title = 'Movimento da torre'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/3N4/8/8/8 w - - 0 1',
                     solution_moves = 'd4f5'
WHERE title = 'Movimento do cavalo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/8/8/8/B7 w - - 0 1',
                     solution_moves = 'a1h8'
WHERE title = 'Movimento do bispo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/8/8/8/3Q4 w - - 0 1',
                     solution_moves = 'd1d8'
WHERE title = 'Movimento da dama'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/4K3/8/8/8 w - - 0 1',
                     solution_moves = 'e4e5'
WHERE title = 'Movimento do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '8/8/8/3p4/4P3/4p3/8/2B5 w - - 0 1',
                     solution_moves = 'e4d5 c1e3'
WHERE title = 'Captura de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

-- =============================================
-- Capítulo 4 — Regras Fundamentais
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Xeque'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Xeque-mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Empate por afogamento'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Empate por repetição'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Regra dos 50 lances'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Material insuficiente'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

-- =============================================
-- Capítulo 5 — Regras Especiais
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Roque pequeno'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Roque grande'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'En passant'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Promoção do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

-- =============================================
-- Capítulo 6 — Princípios Básicos de Abertura
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Controle do centro'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Desenvolvimento das peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Segurança do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Não mover a mesma peça várias vezes'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Conectar as torres'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

-- =============================================
-- Capítulo 7 — Noções Básicas de Tática
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Ataque duplo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Cravada'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Garfo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Descoberta'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Ataque descoberto'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Mate em 1'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Estrutura de peões'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

-- =============================================
-- Capítulo 8 — Como desenvolver o meio-jogo
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Casas fracas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Colunas abertas e semi-abertas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Coordenação de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

-- =============================================
-- Capítulo 9 — Formas de ganhar o final de um jogo
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Oposição de reis'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Rei e peão vs rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Regra do quadrado do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4'
WHERE title = 'Padrões básicos de mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);