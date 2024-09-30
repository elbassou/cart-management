package app.micros.cartservice.feignClient;


import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {
    @GetMapping("api/inventory/{productId}")
    InventoryResponse getInventoryByProductId(@PathVariable("productId") Long productId);

    @PostMapping("api/inventory/update")
    void updateInventory(@RequestBody UpdateInventoryRequest request);
}

