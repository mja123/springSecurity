CREATE SCHEMA IF NOT EXISTS security;
INSERT INTO security.books(title, author, isbn, genre, published_year, created_at)
    VALUES('The Pragmatic Programmer', 'David Thomas', '978-0135957059', 'Technology', 2019, now())
    ON CONFLICT (isbn) DO NOTHING;
INSERT INTO security.books(title, author, isbn, genre, published_year, created_at)
    VALUES('Clean Code', 'Robert C. Martin', '978-0132350884', 'Technology', 2008, now())
    ON CONFLICT (isbn) DO NOTHING;
INSERT INTO security.books(title, author, isbn, genre, published_year, created_at)
    VALUES('Design Patterns', 'Gang of Four', '978-0201633610', 'Technology', 1994, now())
    ON CONFLICT (isbn) DO NOTHING;
INSERT INTO security.books(title, author, isbn, genre, published_year, created_at)
    VALUES('The Hobbit', 'J.R.R. Tolkien', '978-0547928227', 'Fantasy', 1937, now())
    ON CONFLICT (isbn) DO NOTHING;
INSERT INTO security.books(title, author, isbn, genre, published_year, created_at)
    VALUES('Dune', 'Frank Herbert', '978-0441013593', 'Science Fiction', 1965, now())
    ON CONFLICT (isbn) DO NOTHING;
