-- Table des Produits
CREATE TABLE IF NOT EXISTS produit (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         price DOUBLE,
                         inventaire_id BIGINT
);