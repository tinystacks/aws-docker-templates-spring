CREATE TABLE IF NOT EXISTS ITEMS (
    id serial PRIMARY KEY,
    title varchar(255) UNIQUE NOT NULL,
    content varchar(255) NOT NULL
);