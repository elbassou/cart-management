package app.micros.shared.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    public ProductResponse(Long productId, String name, BigDecimal price) {

   this.id = productId;
    this.name = name;
    this.price = price;}
}
