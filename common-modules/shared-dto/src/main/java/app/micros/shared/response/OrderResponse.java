package app.micros.shared.response;



import java.math.BigDecimal;
import java.time.LocalDateTime;

import app.micros.shared.dto.OrderStatusDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long clientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime commandeDate;
   // private OrderStatusDTO status; // Utilisation de l'enum DTO

    private String status; // Utilisation de l'enum DTO
    private BigDecimal totalPrice;

}
