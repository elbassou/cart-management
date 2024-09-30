

package app.micros.cartservice.feignClient;

import app.micros.shared.request.CreateOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", url = "http://localhost:8084") // URL du service Order
public interface OrderClient {

    @PostMapping("/api/orders")
    void createOrder(@RequestBody CreateOrderRequest createOrderRequest);
}
