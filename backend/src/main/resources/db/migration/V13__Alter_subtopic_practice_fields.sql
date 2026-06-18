ALTER TABLE subtopics ADD COLUMN opponent_moves VARCHAR(255) default null;
ALTER TABLE subtopics ADD COLUMN practice_explanation TEXT DEFAULT NULL;

-- =============================================
-- Capítulo 3 — Movimento das Peças
-- =============================================

UPDATE subtopics SET
                     initial_fen = '7k/8/8/8/3P4/8/8/K7 w - - 0 1',
                     solution_moves = 'd4d5',
                     practice_explanation = 'A base de todo o xadrez é saber movimentar as peças que você possui.Agora que o movimento de peão foi aprendido, inicie sua jornada nos tabuleiros de xadrez e faça seu primeiro movimento ao avançar seu peão.'
WHERE title = 'Movimento do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/8/8/8/8/8/8/K2R1B2 w - - 0 1',
                     solution_moves = 'd1d8',
                     practice_explanation = 'Além de saber movimentar as peças, é necessário movimentá-las da melhor forma possível. Use seus conhecimentos sobre o movimento da torre para avançar e atacar o rei adversário com seu próximo lance.'
WHERE title = 'Movimento da torre'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '1r5k/6bb/2Bp2p1/1P3P2/3N4/1R3Q2/2P1P3/BK6 w - - 0 1',
                     solution_moves = 'd4e6',
                     practice_explanation = 'Em partidas de xadrez, a posição do tabuleiro tende a se tornar mais complexa, mesmo peças com muita mobilidade como o cavalo podem acabar tendo seu movimento limitado. Ache o único lance q permite melhorar a posição do seu cavalo.'
WHERE title = 'Movimento do cavalo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '2p4k/3p4/4p3/5p2/6p1/7p/8/K6B w - - 0 1',
                     solution_moves = 'h1a8',
                     practice_explanation = 'Ao mover peças de xadrez, temos que ter cuidado em qual casa colocamos ela, pois ela pode estar sendo dominada por uma peça adversária.Agora que você sabe como movimentar o bispo, mova ele mas sem que seja ameaçado por um peão'
WHERE title = 'Movimento do bispo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/6pr/5p2/4p3/3Q4/8/8/K7 w - - 0 1',
                     solution_moves = 'd4d8',
                     practice_explanation = 'A dama é uma peça extremamente forte dentro do tabuleiro.Use seu próximo lance para tirá-la de uma ameaça, ao mesmo tempo que ataca o rei adversário.'
WHERE title = 'Movimento da dama'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/8/2ppp3/1p3p2/n2K2n1/2N1B3/8/R5R1 w - - 0 1',
                     solution_moves = 'd4d3',
                     practice_explanation = 'Nem sempre a melhor jogada será algo ofensivo, saber a hora de se defender também é muito importante. Use o movimento do seu rei para movê-lo para uma melhor posição no tabuleiro.'
WHERE title = 'Movimento do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET
                     initial_fen = '7k/8/8/2n5/4p3/8/3P4/K5B1 w - - 0 1',
                     solution_moves = 'g1c5,d2e3',
                     opponent_moves = 'e4e3',
                     practice_explanation = 'Você agora sabe como capturar peças adversárias. Faça dois movimentos precisos para conseguir capturar ambas as peças adversárias em poucos movimentos.'
WHERE title = 'Captura de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

-- =============================================
-- Capítulo 4 — Regras Fundamentais
-- =============================================

UPDATE subtopics SET
                     initial_fen = '7k/8/8/8/3K4/8/7B/8 w - - 0 1',
                     solution_moves = 'h2e5',
                     practice_explanation = 'Nesse subtópico foi visto uma das maiores ameaças que você pode fazer ao seu adversário. Faça um xeque com seu bispo para atacar o rei do oponente.'
WHERE title = 'Xeque'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = '8/7k/8/4B3/3K4/8/8/6Q1 w - - 0 1',
                     solution_moves = 'g1h7',
                     practice_explanation = 'Agora você sabe uma das formas mais eficientes de como ganhar uma partida. Aproveite do rei exposto do adversário ganhe esse jogo com um xeque-mate'
WHERE title = 'Xeque-mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = '2k5/2P5/2K5/8/8/8/8/8 w - - 0 1',
                     solution_moves = 'c6d6',
                     practice_explanation = 'Afogar partidas é algo muito perigoso, principalmente quando o jogador possui plena vantagem. Encontre o lance que evita o afogamento e permite a vitória para as brancas.'
WHERE title = 'Empate por afogamento'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET
                     initial_fen = '3k4/3q4/8/8/3Q4/8/3K4/8 w - - 0 1',
                     solution_moves = 'd4d7',
                     practice_explanation = 'Um final entre damas é considerado empatado e as brancas ainda estão em uma posição pior. Há casos onde empatar é a melhor opção, faça o lance que evita a derrota das brancas aproveitando do conceito aprendido nesse capítulo.'
WHERE title = 'Material insuficiente'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

-- =============================================
-- Capítulo 5 — Regras Especiais
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'r1bqkbnr/pppp1pp1/2n4p/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1',
                     solution_moves = 'e1g1',
                     practice_explanation = 'Mover duas peças com uma jogada só é algo muito forte em um jogo de xadrez. Aplique o conceito que foi visto nesse capítulo para conseguir mover mais de uma peça em apenas uma jogada e ainda proteger seu rei'
WHERE title = 'Roque pequeno'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'r1bqk2r/1pppbppp/p1n2n2/4p1B1/4P3/2NP4/PPP1QPPP/R3KBNR w KQkq - 0 1',
                     solution_moves = 'e1c1',
                     practice_explanation = 'Visando uma abordagem mais ofensiva pra seu jogo, use o conceito aprendido nesse subtópico para ter uma postura mais agressiva ao mesmo tempo que se protege.'
WHERE title = 'Roque grande'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = 'r1bq1rk1/2ppbppp/p1n2n2/Pp2p1B1/4P3/2NP4/1PP1QPPP/R3KBNR w KQ - 0 1',
                     solution_moves = 'a5b6',
                     practice_explanation = 'O movimento en passant é um dos movimentos que mais passa desapercebido pelos jogadores. Ache uma situação que você pode utilizar dessa regra especial.'
WHERE title = 'En passant'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET
                     initial_fen = '8/4P1k1/8/3K4/8/8/8/8 w - - 0 1',
                     solution_moves = 'e7e8',
                     practice_explanation = 'Para ganhar um jogo de xadrez é extremamente necessário promover peões para que se tornem peças mais fortes. Faça o movimento que garante a vitória para as brancas por meio desse conceito'
WHERE title = 'Promoção do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

-- =============================================
-- Capítulo 6 — Princípios Básicos de Abertura
-- =============================================

UPDATE subtopics SET
                     initial_fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'e2e4,d2d4',
                     opponent_moves = 'b8c6',
                     practice_explanation = 'O centro é uma das regiões do tabuleiro mais disputada na fase inicial. Avance seus peões de forma correta para garantir o controle desse espaço.'
WHERE title = 'Controle do centro'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'r1bqkb1r/pppppppp/2n2n2/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 0 1',
                     solution_moves = 'b1c3,g1f3,c1d2,f1b5',
                     opponent_moves = 'e7e6,f8b4,d7d5',
                     practice_explanation = 'No início de uma partida de xadrez é extremamente importante que o jogador desenvolva suas peças ao mesmo tempo que defende e cria ameaças para a posição do adversário. Nesse subtópico você deve desenvolver seus cavalos e posteriormente seus bispos, sempre tendo em mente as ameaças que seu oponente está criando, a fim de se defender delas e retrucá-las'
WHERE title = 'Desenvolvimento das peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'rnbqkb1r/pp2pppp/3p1n2/2p5/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 1',
                     solution_moves = 'f1e2,e1g1',
                     opponent_moves = 'e7e6',
                     practice_explanation = 'Proteger o rei é uma das tarefas mais essenciais do início de uma partida de xadrez. Faça a sequência de dois lances que protege o rei de forma mais eficiente, ao mesmo tempo que desenvolve suas peças.'
WHERE title = 'Segurança do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET
                     initial_fen = 'rn1q1rk1/pp1bbppp/3ppn2/2p3B1/4P3/2NP1N2/PPP1BPPP/R2Q1RK1 w - - 0 1',
                     solution_moves = 'd1d2',
                     practice_explanation = 'A abertura das brancas está quase concluída, falta apenas o último passo. Execute o único lance que encerra de vez a fase de abertura das brancas.'
WHERE title = 'Conectar as torres'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

-- =============================================
-- Capítulo 7 — Noções Básicas de Tática
-- =============================================

UPDATE subtopics SET
                     initial_fen = '7k/8/5r2/6q1/8/Q1N5/KP6/8 w - - 0 1',
                     solution_moves = 'c3e4',
                     practice_explanation = 'Utilizar movimentos que envolvem conceitos táticos é muito importante para pegar adversários de surpresa, porém também é necessário que o jogador identifique tais oportunidades. Faça o movimento que realiza um ataque duplo e garante o ganho de uma peça para as brancas.'
WHERE title = 'Ataque duplo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = 'r1bqkbnr/ppp2ppp/2n5/1B2p3/3pP3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1',
                     solution_moves = 'f3e5',
                     practice_explanation = 'Tática está presente em qualquer momento de um jogo de xadrez, por isso é muito importante ter noções básicas sobre ela. Utilize o que foi aprendido nesse subtópico para ganhar uma peça por meio de uma cravada.'
WHERE title = 'Cravada'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = '8/6k1/5q2/8/3P4/2B5/1K6/8 w - - 0 1',
                     solution_moves = 'd4d5',
                     practice_explanation = 'Possibilitar criar ameaças com uma peça mesmo sem mexê-la é o que torna o descoberto tão poderoso. Utilize dele para garantir o ganho de uma peça nesse subtópico.'
WHERE title = 'Descoberto'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = '8/2q5/5k2/8/3N4/2B5/1K6/8 w - - 0 1',
                     solution_moves = 'd4b5',
                     practice_explanation = 'Encontre o moviemento que garante a vitória do jogo para as brancas ao utilizar o conceito de tática aprendido nesse subtópico'
WHERE title = 'Espeto'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET
                     initial_fen = '1k5b/8/1PR5/2K5/PQ2pr2/N7/7q/8 w - - 0 1',
                     solution_moves = 'b4a5',
                     practice_explanation = 'A posição abaixo está emapata, exceto se as brancas jogarem um único lance, que ameaça o mate em 1, ao encontrar esse lance torna-se impossível as pretas ganharem.'
WHERE title = 'Mate em 1'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

-- =============================================
-- Capítulo 8 — Como desenvolver o meio-jogo
-- =============================================

UPDATE subtopics SET
                     initial_fen = '1krq3r/ppp2bpp/4p3/3p1p2/3P4/2N1PNP1/PPPB1PBP/R2Q1RK1 w - - 0 1',
                     solution_moves = 'f3e5,e5c6',
                     opponent_moves = 'b7b6',
                     practice_explanation = 'No meio de jogo é muito importante aproveitar as brechas que o adversário fornece. Utilize das casas fracas que seu oponente disponibilizar e coloque seu cavalo em um posto avançado que permite usá-lo para atacar psoteriormente.'
WHERE title = 'Casas fracas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET
                     initial_fen = 'r3qrk1/p2nbppp/5n2/3p4/3PPB2/2N2NP1/P1P2PBP/R2QK2R w K - 0 1',
                     solution_moves = 'a1a2,b1b7',
                     opponent_moves = 'h7h6',
                     practice_explanation = 'Aproveite o tema estratégico que está presente nesse tabuleiro e foi aprendido nesse subtópico para melhorar a posição de sua torre.'
WHERE title = 'Colunas abertas e semi-abertas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET
                     initial_fen = '6kr/pR1n1pp1/5r1q/3p4/3P4/6P1/P1PN1P1N/3Q1RK1 w - - 0 1',
                     solution_moves = 'd2f3',
                     practice_explanation = 'Você está prestes a tomar um xeque mate. A única forma de contornar essa situação é coodenando outra peça com o seu cavalo que está sendo ameaçado, para que assim uma proteja a outra e o seu rei fique mais protegido.'
WHERE title = 'Coordenação de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

-- =============================================
-- Capítulo 9 — Formas de ganhar o final de um jogo
-- =============================================

UPDATE subtopics SET
                     initial_fen = '8/8/2p1p1pp/8/4kPP1/1PP1P3/8/4K3 w - - 0 1',
                     solution_moves = 'e1e2',
                     practice_explanation = 'Encontre o movimento que utiliza o conceito que foi visto nesse subtópico para evitar que o rei adversário possa invadir sua posição e começar a capturar seus peões.'
WHERE title = 'Oposição de reis'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/2P2k2/3K4/8/8 w - - 0 1',
                     solution_moves = 'd3d4,d4d5,d5d6,d6d7,c4c5,c5c6,c6c7,c7c8',
                     opponent_moves = 'f4f5,f5f6,f6f7,f7f6,f6e5,e5d5,d5c5',
                     practice_explanation = 'Esse final é um dos mais simples, mas ao mesmo tempo exige moviementos precisos do jogador. Encontre esses movimentos ate conseguir promover seu peão e tornar o final ganho.'
WHERE title = 'Rei e peão vs rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET
                     initial_fen = '8/2k5/8/P7/4P2P/8/8/3K4 w - - 0 1',
                     solution_moves = 'h4h5',
                     practice_explanation = 'É muito importante saber qual peão será o escolhido para avançar e ser promovido. O jogador deve tomar essa decisão o quanto antes, faça apenas o primeiro movimento para indicar qual peão deve ser o escolhido para promover.'
WHERE title = 'Regra do quadrado do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET
                     initial_fen = '8/8/8/8/7k/1Q6/2R5/K7 w - - 0 1',
                     solution_moves = 'c2c4,b3b5,c4c6,b5b7,c6c8',
                     opponent_moves = 'h4h5,h5h6,h6h7,h7h8',
                     practice_explanation = 'Nesse subtópico foi ensinado uma das formas mais mais rápidas de garantir uma vitória. Use o mate escadinha para ganhar essa partida de xadrez.'
WHERE title = 'Padrões básicos de mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);