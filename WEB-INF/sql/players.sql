DROP TABLE IF EXISTS players;

CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    firstname VARCHAR(50),
    position VARCHAR(3),
    age INTEGER,
    nationality VARCHAR(50),
    team VARCHAR(50),
    jersey INTEGER,
    height INTEGER,
    birthdate DATE,
    birthplace VARCHAR(50),
    strong_foot VARCHAR(50),
    info_path TEXT
)