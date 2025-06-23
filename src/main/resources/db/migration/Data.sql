INSERT INTO users (id, name, surname, phone, password)
VALUES (
           '00000000-0000-0000-0000-000000000001',
           'Admin',
           'User',
           '+10000000000',
           '$2a$10$7sE3V7r5HhR7N2FwqXcqzuNHWQvB1MtUYpBZ7ZZ5tqKKydzGpR2Vq'
       );

INSERT INTO user_roles (user_id, role)
VALUES ('00000000-0000-0000-0000-000000000001', 'ADMIN');