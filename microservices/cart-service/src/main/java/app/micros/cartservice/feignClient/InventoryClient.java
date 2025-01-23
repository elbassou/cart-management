package app.micros.cartservice.feignClient;


import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {
    @GetMapping("api/inventory/{productId}")
    InventoryResponse getInventoryByProductId(@PathVariable("productId") Long productId);

    @PutMapping("api/inventory/update")
    void updateInventory(@RequestBody UpdateInventoryRequest request);
}

