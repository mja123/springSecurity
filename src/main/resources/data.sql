INSERT INTO users(name, lastname, email, created_at)
    VALUES('Matias', 'Aguilar', 'mati@gmail.com', now())
    ON CONFLICT (email) DO NOTHING;
INSERT INTO users(name, lastname, email, created_at)
    VALUES('Milagros', 'Murua', 'mili@gmail.com', now())
    ON CONFLICT (email) DO NOTHING;
INSERT INTO users(name, lastname, email, created_at)
    VALUES('Zoka', 'Aguilar', 'zoka@gmail.com', now())
    ON CONFLICT (email) DO NOTHING;
INSERT INTO users(name, lastname, email, created_at)
    VALUES('Snor', 'Aguilar', 'snor@gmail.com', now())
    ON CONFLICT (email) DO NOTHING;
INSERT INTO users(name, lastname, email, created_at)
    VALUES('Belly', 'Aguilar', 'belly@gmail.com', now())
    ON CONFLICT (email) DO NOTHING;