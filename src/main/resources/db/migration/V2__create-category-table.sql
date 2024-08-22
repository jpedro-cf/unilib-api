CREATE TABLE books_category (
    id UUID PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT
);

CREATE TABLE book_category_mapping (
    book_id UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES books_category(id) ON DELETE CASCADE
);