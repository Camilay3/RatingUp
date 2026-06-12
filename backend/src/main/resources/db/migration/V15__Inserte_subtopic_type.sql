UPDATE subtopics
SET type = 'MULTIPLE_CHOICE'
WHERE
   -- Capítulo 1
    (title = 'O que é o xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))
   OR (title = 'Objetivo do jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))
   OR (title = 'História do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))
   OR (title = 'Como o xadrez se tornou esporte' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))
   OR (title = 'O que é Rating' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))
   OR (title = 'Vocabulário básico do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))
   OR (title = 'Vocabulário popular do xadrez' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1))

   -- Capítulo 2
   OR (title = 'Estrutura do tabuleiro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))
   OR (title = 'Casas claras e escuras' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))
   OR (title = 'Colunas, fileiras e diagonais' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))
   OR (title = 'Nomeação das casas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))
   OR (title = 'Conhecendo as peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))
   OR (title = 'Valor relativo das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2))

   -- Capítulo 4
   OR (title = 'Tempo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))

   -- Capítulo 6
   OR (title = 'Não mover a mesma peça várias vezes' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))
   OR (title = 'Padronização de aberturas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))

   -- Capítulo 7
   OR (title = 'Estrutura de peões' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))
   OR (title = 'CCT (Checks, Captures, Threats)' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))

   -- Capítulo 8
   OR (title = 'O que é o meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))
   OR (title = 'Planos no meio-jogo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))
   OR (title = 'Ataque ao rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))
   OR (title = 'Peças boas vs ruins' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))

   -- Capítulo 9
   OR (title = 'O que é o final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))
   OR (title = 'Tipos de final' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9));

UPDATE subtopics
SET type = 'BOARD'
WHERE
   -- Capítulo 3
    (title = 'Movimento do peão' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))
   OR (title = 'Movimento da torre' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))
   OR (title = 'Movimento do cavalo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))
   OR (title = 'Movimento do bispo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))
   OR (title = 'Movimento da dama' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))
   OR (title = 'Movimento do rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))
   OR (title = 'Captura de peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3))

   -- Capítulo 4
   OR (title = 'Xeque' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))
   OR (title = 'Xeque-mate' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))
   OR (title = 'Empate por afogamento' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))
   OR (title = 'Empate por repetição' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))
   OR (title = 'Regra dos 50 lances' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))
   OR (title = 'Material insuficiente' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4))

   -- Capítulo 5
   OR (title = 'Roque pequeno' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5))
   OR (title = 'Roque grande' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5))
   OR (title = 'En passant' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5))
   OR (title = 'Promoção do peão' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5))

   -- Capítulo 6
   OR (title = 'Controle do centro' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))
   OR (title = 'Desenvolvimento das peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))
   OR (title = 'Segurança do rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))
   OR (title = 'Conectar as torres' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6))

   -- Capítulo 7
   OR (title = 'Ataque duplo' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))
   OR (title = 'Cravada' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))
   OR (title = 'Descoberto' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))
   OR (title = 'Espeto' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))
   OR (title = 'Mate em 1' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7))

   -- Capítulo 8
   OR (title = 'Casas fracas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))
   OR (title = 'Colunas abertas e semi-abertas' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))
   OR (title = 'Coordenação de peças' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8))

   -- Capítulo 9
   OR (title = 'Oposição de reis' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))
   OR (title = 'Rei e peão vs rei' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))
   OR (title = 'Regra do quadrado do peão' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9))
   OR (title = 'Padrões básicos de mate' AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9));