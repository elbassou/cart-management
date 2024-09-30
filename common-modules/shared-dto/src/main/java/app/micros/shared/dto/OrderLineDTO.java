package app.micros.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDTO {
    private Long produitId; // Référence à l'ID du produit
    private int quantity;    // Quantité de produit commandée
    private BigDecimal price; // Prix du produit à ce moment de la commande
}