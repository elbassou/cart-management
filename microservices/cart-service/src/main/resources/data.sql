
-- Insertion de Panier
INSERT INTO cart (client_id, date_achat) VALUES
                                             (1, NOW()),
                                             (2, NOW());
INSERT INTO Cart (client_id) VALUES
                                     (1001),
                                     (1002);
-- Insertion d'Items du Panier
INSERT INTO cart_cart_Item_Ids (cart_id, cart_Item_id) VALUES
                                                         (1, 1),
                                                         (1, 2),
                                                         (2, 1);




-- Insérer des éléments de panier
INSERT INTO Cart_Item (cart_id, produit_id, quantity, price) VALUES
                                                                 (1, 1, 2, 10.00), -- 2 unités du Produit A
                                                                 (1, 2, 1, 15.00), -- 1 unité du Produit B
                                                                 (2, 2, 3, 15.00), -- 3 unités du Produit B pour le panier 2
                                                                 (2, 3, 1, 20.00); -- 1 unité du Produit C pour le panier 2