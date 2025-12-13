CREATE TABLE IF NOT EXISTS members(
    username VARCHAR(16) PRIMARY KEY,
    name VARCHAR(50),
    password VARCHAR(50),
    gender VARCHAR(1),
    favorite TEXT
)