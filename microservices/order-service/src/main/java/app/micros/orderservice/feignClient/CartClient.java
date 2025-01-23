package app.micros.orderservice.feignClient;


import app.micros.orderservice.config.FeignConfig;
import app.micros.shared.response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service" , url = "http://localhost:8082",configuration = FeignConfig.class)
public interface CartClient {

    @GetMapping("/api/cart/client/{clientId}")
    CartResponse getCartByClientId(@PathVariable("clientId") Long clientId);
}
