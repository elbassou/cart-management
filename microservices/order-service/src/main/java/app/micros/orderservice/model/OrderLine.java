package app.micros.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_lines") // La table dans la base de données reste 'order_lines'
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "commande_id", nullable = false)
    private Long commandeId; // Référence à la commande à laquelle cette ligne appartient

    @Column(name = "produit_id")
    private Long produitId; // Référence à l'ID du produit, pourrait être obtenu du Product Service

    private int quantity; // Quantité de produit commandée
    private BigDecimal price; // Prix du produit à ce moment de la commande

  public  OrderLine(Long produitId,int quantity,BigDecimal price) {
        this.produitId = produitId;
        this.quantity = quantity;
        this.price = price;
    }


}

