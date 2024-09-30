package app.micros.shared.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long id; // ID de l'article du panier
    private Long productId;// ID du produit
    private int quantity; // Quantité commandée
    private BigDecimal price; // Prix de l'article


}