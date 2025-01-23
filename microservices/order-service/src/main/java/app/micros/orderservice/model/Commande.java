package app.micros.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId; // Référence directe à l'ID du client

    @Column(name = "cart_id")
    private Long cartId; // Utilisation d'une référence directe à l'ID du panier associé

    private LocalDateTime commandeDate; // Date de la commande

    // Remplacez la liste des lignes de commande par une liste d'IDs de commande
    @ElementCollection
    @CollectionTable(name = "order_lines", joinColumns = @JoinColumn(name = "commande_id"))
    @Column(name = "orderLine_id")
    private List<Long> orderLineIds; // Utilisation d'une liste d'IDs de lignes de commande

    @Enumerated(EnumType.STRING)
    private CommandeStatus status;

    private BigDecimal totalPrice;



    // Getters and Setters
}


