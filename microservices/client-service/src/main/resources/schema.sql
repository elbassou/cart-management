-- Table des Clients
CREATE TABLE IF NOT EXISTS clients (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         name VARCHAR(255),
                         address VARCHAR(255),
                         last_login TIMESTAMP
);