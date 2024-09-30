package app.micros.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long clientId;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalAmount;

    // Getters and Setters
}
