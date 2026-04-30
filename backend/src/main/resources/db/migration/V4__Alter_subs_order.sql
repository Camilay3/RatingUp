UPDATE subtopico SET ordem = 4, titulo = 'Empate por repetição' WHERE titulo = 'Casos de empate';
UPDATE subtopico SET ordem = 5 WHERE titulo = 'Regra dos 50 lances';
UPDATE subtopico SET ordem = 6 WHERE titulo = 'Material insuficiente';
UPDATE subtopico SET ordem = 7 WHERE titulo = 'Tempo';
INSERT INTO subtopico (ordem, titulo, capitulo_id)
VALUES (3, 'Empate por afogamento', (SELECT id FROM capitulo WHERE ordem = 4));