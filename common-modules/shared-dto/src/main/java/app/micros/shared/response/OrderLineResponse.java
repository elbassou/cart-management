package app.micros.shared.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineResponse {
    private Long id;          // ID de la ligne de commande
    private Long produitId;   // Référence à l'ID du produit
    private int quantity;     // Quantité de produit commandée
    private BigDecimal price;  // Prix du produit à ce moment de la commande
}
