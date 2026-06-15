ALTER TABLE subtopics ADD COLUMN opponent_moves VARCHAR(255);

-- =============================================
-- Capítulo 3 — Movimento das Peças
-- =============================================

UPDATE subtopics SET
                     initial_fen = '7k/8/8/8/3P4/8/8/K7 w - - 0 1',
                     solution_moves = 'd4d5'
WHERE title = 'Movimento do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/8/8/8/8/8/8/K2R1B2 w - - 0 1',
                     solution_moves = 'd1d8'
WHERE title = 'Movimento da torre'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '1r5k/6bb/2Bp2p1/1P3P2/3N4/1R3Q2/2P1P3/BK6 w - - 0 1',
                     solution_moves = 'd4e6'
WHERE title = 'Movimento do cavalo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '2p4k/3p4/4p3/5p2/6p1/7p/8/K6B w - - 0 1',
                     solution_moves = 'h1a8'
WHERE title = 'Movimento do bispo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/6pr/5p2/4p3/3Q4/8/8/K7 w - - 0 1',
                     solution_moves = 'd4d8'
WHERE title = 'Movimento da dama'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/8/2ppp3/1p3p2/n2K2n1/2N1B3/8/R5R1 w - - 0 1',
                     solution_moves = 'd4d3'
WHERE title = 'Movimento do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/8/8/2n5/4p3/8/3P4/K5B1 w - - 0 1',
                     solution_moves = 'g1c5,d2e3',
                     opponent_moves = 'e4e3'
WHERE title = 'Captura de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

-- =============================================
-- Capítulo 4 — Regras Fundamentais
-- =============================================

UPDATE subtopics SET
                     initial_fen = '7k/8/8/8/3K4/8/7B/8 w - - 0 1',
                     solution_moves = 'h2e5'
WHERE title = 'Xeque'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = '8/7k/8/4B3/3K4/8/8/6Q1 w - - 0 1',
                     solution_moves = 'g1h7'
WHERE title = 'Xeque-mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = '2k5/2P5/2K5/8/8/8/8/8 w - - 0 1',
                     solution_moves = 'c6d6'
WHERE title = 'Empate por afogamento'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = '3k4/3q4/8/8/3Q4/8/3K4/8 w - - 0 1',
                     solution_moves = 'd4d7'
WHERE title = 'Material insuficiente'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

-- =============================================
-- Capítulo 5 — Regras Especiais
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'r1bqkbnr/pppp1pp1/2n4p/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1',
                     solution_moves = 'e1g1'
WHERE title = 'Roque pequeno'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'r1bqk2r/1pppbppp/p1n2n2/4p1B1/4P3/2NP4/PPP1QPPP/R3KBNR w KQkq - 0 1',
                     solution_moves = 'e1c1'
WHERE title = 'Roque grande'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'r1bq1rk1/2ppbppp/p1n2n2/Pp2p1B1/4P3/2NP4/1PP1QPPP/R3KBNR w KQ - 0 1',
                     solution_moves = 'a5b6'
WHERE title = 'En passant'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = '8/4P1k1/8/3K4/8/8/8/8 w - - 0 1',
                     solution_moves = 'e7e8'
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