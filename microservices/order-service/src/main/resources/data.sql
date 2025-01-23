
-- Insertion de Commandes
INSERT INTO commande (client_id, cart_id, commande_date, status, total_price) VALUES
                                                                                  (1, 1, '2024-10-01T13:20:03.741672400', 'EN_ATTENTE', 31.48),
                                                                                  (2, 2, '2024-10-01T13:20:03.741672400', 'CONFIRME', 10.99);

-- Insertion de Lignes de Commande
INSERT INTO order_lines (commande_id, produit_id, quantity, price,order_line_id) VALUES
                                                                       (1, 1, 12, 10.99,1),
                                                                       (1, 2, 7, 20.49,2),
                                                                       (2, 1, 4, 10.99,1);