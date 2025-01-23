package app.micros.cartservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="Cart_Item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cart_id")
    private Long cartId;   // refrence  Cart cart


    @Column(name = "produit_id")
    private Long productId;  // ref Produit produit;

    private int quantity;
    private BigDecimal price; // Prix du produit au moment de l'ajout

    // Getters and Setters
}

