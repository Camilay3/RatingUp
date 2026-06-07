-- Capítulo 7
DELETE FROM subtopics
WHERE title = 'Garfo'
  AND chapter_id = (
    SELECT id
    FROM chapters
    WHERE display_order = 7
);

UPDATE subtopics
SET title = 'Descoberto'
WHERE title = 'Descoberta'
  AND chapter_id = (
    SELECT id FROM chapters WHERE display_order = 7
);

UPDATE subtopics
SET title = 'Espeto'
WHERE title = 'Ataque descoberto'
  AND chapter_id = (
    SELECT id FROM chapters WHERE display_order = 7
);

-- Capítulo 8
UPDATE subtopics
SET title = 'Peças boas vs ruins'
WHERE title = 'Peças boas vs peças ruins'
  AND chapter_id = (
    SELECT id FROM chapters WHERE display_order = 8
);

UPDATE subtopics
SET display_order = 3
WHERE title = 'Descoberto'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics
SET display_order = 4
WHERE title = 'Espeto'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics
SET display_order = 5
WHERE title = 'Mate em 1'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics
SET display_order = 6
WHERE title = 'Estrutura de peões'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics
SET display_order = 7
WHERE title = 'CCT (Checks, Captures, Threats)'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);