-- Table des Panier
CREATE TABLE IF NOT EXISTS cart (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      client_id BIGINT NOT NULL,
                      date_achat TIMESTAMP
);

-- Table des Items du Panier
CREATE TABLE IF NOT EXISTS cart_cart_Item_Ids (
                                   cart_id BIGINT,
                                   cart_Item_id BIGINT,
                                   PRIMARY KEY (cart_id, cart_Item_id)
);


CREATE TABLE IF NOT EXISTS Cart_Item (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         cart_id BIGINT NOT NULL,
                                         produit_id BIGINT NOT NULL,
                                         quantity INT NOT NULL,
                                         price DECIMAL(10, 2) NOT NULL
    );