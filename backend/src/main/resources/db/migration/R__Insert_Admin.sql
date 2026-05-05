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