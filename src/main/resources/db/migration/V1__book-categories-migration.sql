CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE book (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    description TEXT,
    image VARCHAR(255),
    pdf VARCHAR(255),
    has_ebook BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id UUID PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT
);

CREATE TABLE book_category (
    book_id UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);
