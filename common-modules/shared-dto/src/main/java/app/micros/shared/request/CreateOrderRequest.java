package app.micros.shared.request;

import app.micros.shared.response.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private Long clientId;
    private List<CartItemResponse> items =new ArrayList<>();
}
