INSERT INTO subtopics (display_order, title, chapter_id)
-- Subtópicos do capítulo 1 (Introdução ao Xadrez)
    SELECT 1, 'O que é o xadrez', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL
    SELECT 2, 'Objetivo do jogo', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL
    SELECT 3, 'História do xadrez', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL
    SELECT 4, 'Como o xadrez se tornou esporte', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL
    SELECT 5, 'O que é Rating', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL
    SELECT 6, 'Vocabulário básico do xadrez', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL
    SELECT 7, 'Vocabulário popular do xadrez', id FROM chapters WHERE chapters.display_order = 1
    UNION ALL

-- Subtópicos do capítulo 2 (O Tabuleiro e as peças)
    SELECT 1, 'Estrutura do tabuleiro', id FROM chapters WHERE chapters.display_order = 2
    UNION ALL
    SELECT 2, 'Casas claras e escuras', id FROM chapters WHERE chapters.display_order = 2
    UNION ALL
    SELECT 3, 'Colunas, fileiras e diagonais', id FROM chapters WHERE chapters.display_order = 2
    UNION ALL
    SELECT 4, 'Nomeação das casas', id FROM chapters WHERE chapters.display_order = 2
    UNION ALL
    SELECT 5, 'Conhecendo as peças', id FROM chapters WHERE chapters.display_order = 2
    UNION ALL
    SELECT 6, 'Valor relativo das peças', id FROM chapters WHERE chapters.display_order = 2
    UNION ALL

-- Subtópicos do capítulo 3 (Movimento das peças)
    SELECT 1, 'Movimento do peão', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL
    SELECT 2, 'Movimento da torre', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL
    SELECT 3, 'Movimento do cavalo', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL
    SELECT 4, 'Movimento do bispo', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL
    SELECT 5, 'Movimento da dama', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL
    SELECT 6, 'Movimento do rei', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL
    SELECT 7, 'Captura de peças', id FROM chapters WHERE chapters.display_order = 3
    UNION ALL

-- Subtópicos do capítulo 4 (Regras Fundamentais)
    SELECT 1, 'Xeque', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL
    SELECT 2, 'Xeque-mate', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL
    SELECT 3, 'Empate por afogamento', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL
    SELECT 4, 'Empate por repetição', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL
    SELECT 5, 'Regra dos 50 lances', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL
    SELECT 6, 'Material insuficiente', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL
    SELECT 7, 'Tempo', id FROM chapters WHERE chapters.display_order = 4
    UNION ALL

-- Subtópicos do capítulo 5 (Regras Especiais)
    SELECT 1, 'Roque pequeno', id FROM chapters WHERE chapters.display_order = 5
    UNION ALL
    SELECT 2, 'Roque grande', id FROM chapters WHERE chapters.display_order = 5
    UNION ALL
    SELECT 3, 'En passant', id FROM chapters WHERE chapters.display_order = 5
    UNION ALL
    SELECT 4, 'Promoção do peão', id FROM chapters WHERE chapters.display_order = 5
    UNION ALL

-- Subtópicos do capítulo 6 (Princípios Básicos de Abertura)
    SELECT 1, 'Controle do centro', id FROM chapters WHERE chapters.display_order = 6
    UNION ALL
    SELECT 2, 'Desenvolvimento das peças', id FROM chapters WHERE chapters.display_order = 6
    UNION ALL
    SELECT 3, 'Segurança do rei', id FROM chapters WHERE chapters.display_order = 6
    UNION ALL
    SELECT 4, 'Não mover a mesma peça várias vezes', id FROM chapters WHERE chapters.display_order = 6
    UNION ALL
    SELECT 5, 'Conectar as torres', id FROM chapters WHERE chapters.display_order = 6
    UNION ALL
    SELECT 6, 'Padronização de aberturas', id FROM chapters WHERE chapters.display_order = 6
    UNION ALL

-- Subtópicos do capítulo 7 (Noções Básicas de Tática)
    SELECT 1, 'Ataque duplo', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 2, 'Cravada', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 3, 'Garfo', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 4, 'Descoberta', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 5, 'Ataque descoberto', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 6, 'Mate em 1', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 7, 'Estrutura de peões', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL
    SELECT 8, 'CCT (Checks, Captures, Threats)', id FROM chapters WHERE chapters.display_order = 7
    UNION ALL

-- Subtópicos do capítulo 8 (Como desenvolver o meio-jogo)
    SELECT 1, 'O que é o meio-jogo', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL
    SELECT 2, 'Planos no meio-jogo', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL
    SELECT 3, 'Ataque ao rei', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL
    SELECT 4, 'Casas fracas', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL
    SELECT 5, 'Colunas abertas e semi-abertas', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL
    SELECT 6, 'Peças boas vs peças ruins', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL
    SELECT 7, 'Coordenação de peças', id FROM chapters WHERE chapters.display_order = 8
    UNION ALL

-- Subtópicos do capítulo 9 (Formas de ganhar o final de um jogo)
    SELECT 1, 'O que é o final', id FROM chapters WHERE chapters.display_order = 9
    UNION ALL
    SELECT 2, 'Tipos de final', id FROM chapters WHERE chapters.display_order = 9
    UNION ALL
    SELECT 3, 'Oposição de reis', id FROM chapters WHERE chapters.display_order = 9
    UNION ALL
    SELECT 4, 'Rei e peão vs rei', id FROM chapters WHERE chapters.display_order = 9
    UNION ALL
    SELECT 5, 'Regra do quadrado do peão', id FROM chapters WHERE chapters.display_order = 9
    UNION ALL
    SELECT 6, 'Padrões básicos de mate', id FROM chapters WHERE chapters.display_order = 9;