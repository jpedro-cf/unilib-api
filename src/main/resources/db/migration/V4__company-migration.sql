CREATE TABLE company (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE permission (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    company_id UUID NOT NULL,
    admin BOOLEAN,
    manager BOOLEAN,
    editor BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (company_id) REFERENCES company(id)
);

ALTER TABLE book
ADD COLUMN company_id UUID NOT NULL;

-- Adicionar a restrição de chave estrangeira
ALTER TABLE book
ADD CONSTRAINT fk_company
FOREIGN KEY (company_id)
REFERENCES company(id);