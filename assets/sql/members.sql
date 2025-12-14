DROP TABLE IF EXISTS members;

CREATE TABLE members(
    username VARCHAR(16) PRIMARY KEY,
    name VARCHAR(50),
    password VARCHAR(50),
    favorite TEXT
);