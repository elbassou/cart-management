-- Table de l'Inventaire
CREATE TABLE IF NOT EXISTS inventaire (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            produit_id BIGINT,
                            quantity_available INT

);