package app.micros.shared.response;

import app.micros.shared.dto.OrderLineDTO;
import app.micros.shared.dto.OrderStatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {

    private Long id;
    private Long clientId;
    private Long cartId;
    private LocalDateTime commandeDate;
    private OrderStatusDTO status; // Utilisation de l'enum DTO
    private BigDecimal totalPrice;
    private List<OrderLineDTO> orderLines; // Liste des lignes de commande
}
