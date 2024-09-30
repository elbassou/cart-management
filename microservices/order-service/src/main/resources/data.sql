
-- Insertion de Commandes
INSERT INTO commande (client_id, cart_id, commande_date, status, total_price) VALUES
                                                                                  (1, 1, NOW(), 'EN_ATTENTE', 31.48),
                                                                                  (2, 2, NOW(), 'CONFIRME', 10.99);

-- Insertion de Lignes de Commande
INSERT INTO order_lines (commande_id, produit_id, quantity, price) VALUES
                                                                       (1, 1, 1, 10.99),
                                                                       (1, 2, 1, 20.49),
                                                                       (2, 1, 1, 10.99);