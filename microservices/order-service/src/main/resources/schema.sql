-- Table des Commandes
CREATE TABLE IF NOT EXISTS commande (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          client_id BIGINT NOT NULL,
                          cart_id BIGINT NOT NULL,
                          commande_date TIMESTAMP,
                          status VARCHAR(255),
                          total_price DECIMAL(10, 2)
);

-- Table des Lignes de Commande
CREATE TABLE IF NOT EXISTS order_lines (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             commande_id BIGINT NOT NULL,
                             produit_id BIGINT NOT NULL,
                             order_line_id BIGINT ,
                             quantity INT,
                             price DECIMAL(10, 2)
);