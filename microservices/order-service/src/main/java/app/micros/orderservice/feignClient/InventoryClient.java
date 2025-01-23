package app.micros.orderservice.feignClient;



import app.micros.shared.request.UpdateInventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "http://localhost:8083") // URL du service inventaire
public interface InventoryClient {

    @PutMapping("/api/inventory/update")
    void updateInventory(@RequestBody UpdateInventoryRequest updateRequest);
}
