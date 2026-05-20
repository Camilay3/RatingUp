INSERT INTO users (name, nickname, email, telefone, password, role)
VALUES (
        'Administrador',
        'Master',
        'ratingupadmin@gmail.com',
        '8599998888',
        '$2a$10$xyTt8KT9Qzza60DqXaRg..faEUSULs1DAgunQLmYkbgg..wPIHRm.',
        'ADMIN'
       )
ON CONFLICT (email) DO NOTHING;

INSERT INTO progress (chapters, subtopics, user_id)
VALUES (
        9,
        6,
        (SELECT u.id FROM users u WHERE u.email = 'ratingupadmin@gmail.com')
       )
ON CONFLICT (user_id) DO NOTHING;