package app.micros.cartservice.feignClient;

import app.micros.shared.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long productId);
}