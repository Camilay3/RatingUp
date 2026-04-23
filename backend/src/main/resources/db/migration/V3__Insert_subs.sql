INSERT INTO subtopico (ordem, titulo, capitulo_id)
-- Subtópicos do capítulo 1 (Introdução ao Xadrez)
    SELECT 1, 'O que é o xadrez', id FROM capitulo WHERE ordem = 1
    UNION ALL
    SELECT 2, 'Objetivo do jogo', id FROM capitulo WHERE ordem = 1
    UNION ALL
    SELECT 3, 'História do xadrez', id FROM capitulo WHERE ordem = 1
    UNION ALL
    SELECT 4, 'Como o xadrez se tornou esporte', id FROM capitulo WHERE ordem = 1
    UNION ALL
    SELECT 5, 'O que é Rating', id FROM capitulo WHERE ordem = 1
    UNION ALL
    SELECT 6, 'Vocabulário básico do xadrez', id FROM capitulo WHERE ordem = 1
    UNION ALL
    SELECT 7, 'Vocabulário popular do xadrez', id FROM capitulo WHERE ordem = 1
    UNION ALL

-- Subtópicos do capítulo 2 (O Tabuleiro e as peças)
    SELECT 1, 'Estrutura do tabuleiro', id FROM capitulo WHERE ordem = 2
    UNION ALL
    SELECT 2, 'Casas claras e escuras', id FROM capitulo WHERE ordem = 2
    UNION ALL
    SELECT 3, 'Colunas, fileiras e diagonais', id FROM capitulo WHERE ordem = 2
    UNION ALL
    SELECT 4, 'Nomeação das casas', id FROM capitulo WHERE ordem = 2
    UNION ALL
    SELECT 5, 'Conhecendo as peças', id FROM capitulo WHERE ordem = 2
    UNION ALL
    SELECT 6, 'Valor relativo das peças', id FROM capitulo WHERE ordem = 2
    UNION ALL

-- Subtópicos do capítulo 3 (Movimento das peças)
    SELECT 1, 'Movimento do peão', id FROM capitulo WHERE ordem = 3
    UNION ALL
    SELECT 2, 'Movimento da torre', id FROM capitulo WHERE ordem = 3
    UNION ALL
    SELECT 3, 'Movimento do cavalo', id FROM capitulo WHERE ordem = 3
    UNION ALL
    SELECT 4, 'Movimento do bispo', id FROM capitulo WHERE ordem = 3
    UNION ALL
    SELECT 5, 'Movimento da dama', id FROM capitulo WHERE ordem = 3
    UNION ALL
    SELECT 6, 'Movimento do rei', id FROM capitulo WHERE ordem = 3
    UNION ALL
    SELECT 7, 'Captura de peças', id FROM capitulo WHERE ordem = 3
    UNION ALL

-- Subtópicos do capítulo 4 (Regras Fundamentais)
    SELECT 1, 'Xeque', id FROM capitulo WHERE ordem = 4
    UNION ALL
    SELECT 2, 'Xeque-mate', id FROM capitulo WHERE ordem = 4
    UNION ALL
    SELECT 3, 'Casos de Empate', id FROM capitulo WHERE ordem = 4
    UNION ALL
    SELECT 4, 'Regra dos 50 lances', id FROM capitulo WHERE ordem = 4
    UNION ALL
    SELECT 5, 'Material insuficiente', id FROM capitulo WHERE ordem = 4
    UNION ALL
    SELECT 6, 'Tempo', id FROM capitulo WHERE ordem = 4
    UNION ALL

-- Subtópicos do capítulo 5 (Regras Especiais)
    SELECT 1, 'Roque pequeno', id FROM capitulo WHERE ordem = 5
    UNION ALL
    SELECT 2, 'Roque grande', id FROM capitulo WHERE ordem = 5
    UNION ALL
    SELECT 3, 'En passant', id FROM capitulo WHERE ordem = 5
    UNION ALL
    SELECT 4, 'Promoção do peão', id FROM capitulo WHERE ordem = 5
    UNION ALL

-- Subtópicos do capítulo 6 (Princípios Básicos de Abertura)
    SELECT 1, 'Controle do centro', id FROM capitulo WHERE ordem = 6
    UNION ALL
    SELECT 2, 'Desenvolvimento das peças', id FROM capitulo WHERE ordem = 6
    UNION ALL
    SELECT 3, 'Segurança do rei', id FROM capitulo WHERE ordem = 6
    UNION ALL
    SELECT 4, 'Não mover a mesma peça várias vezes', id FROM capitulo WHERE ordem = 6
    UNION ALL
    SELECT 5, 'Conectar as torres', id FROM capitulo WHERE ordem = 6
    UNION ALL
    SELECT 6, 'Padronização de aberturas', id FROM capitulo WHERE ordem = 6
    UNION ALL

-- Subtópicos do capítulo 7 (Noções Básicas de Tática)
    SELECT 1, 'Ataque duplo', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 2, 'Cravada', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 3, 'Garfo', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 4, 'Descoberta', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 5, 'Ataque descoberto', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 6, 'Mate em 1', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 7, 'Estrutura de peões', id FROM capitulo WHERE ordem = 7
    UNION ALL
    SELECT 8, 'CCT (Checks, Captures, Threats)', id FROM capitulo WHERE ordem = 7
    UNION ALL

-- Subtópicos do capítulo 8 (Como desenvolver o meio-jogo)
    SELECT 1, 'O que é o meio-jogo', id FROM capitulo WHERE ordem = 8
    UNION ALL
    SELECT 2, 'Planos no meio-jogo', id FROM capitulo WHERE ordem = 8
    UNION ALL
    SELECT 3, 'Ataque ao rei', id FROM capitulo WHERE ordem = 8
    UNION ALL
    SELECT 4, 'Casas fracas', id FROM capitulo WHERE ordem = 8
    UNION ALL
    SELECT 5, 'Colunas abertas e semi-abertas', id FROM capitulo WHERE ordem = 8
    UNION ALL
    SELECT 6, 'Peças boas vs peças ruins', id FROM capitulo WHERE ordem = 8
    UNION ALL
    SELECT 7, 'Coordenação de peças', id FROM capitulo WHERE ordem = 8
    UNION ALL

-- Subtópicos do capítulo 9 (Formas de ganhar o final de um jogo)
    SELECT 1, 'O que é o final', id FROM capitulo WHERE ordem = 9
    UNION ALL
    SELECT 2, 'Tipos de final', id FROM capitulo WHERE ordem = 9
    UNION ALL
    SELECT 3, 'Oposição de reis', id FROM capitulo WHERE ordem = 9
    UNION ALL
    SELECT 4, 'Rei e peão vs rei', id FROM capitulo WHERE ordem = 9
    UNION ALL
    SELECT 5, 'Regra do quadrado do peão', id FROM capitulo WHERE ordem = 9
    UNION ALL
    SELECT 6, 'Padrões básicos de mate', id FROM capitulo WHERE ordem = 9;