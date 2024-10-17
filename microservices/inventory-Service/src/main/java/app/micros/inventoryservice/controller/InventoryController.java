package app.micros.inventoryservice.controller;

import app.micros.inventoryservice.service.InventoryService;
import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Endpoint pour récupérer l'inventaire par ID de produit
    @GetMapping("/{productId}")
    public InventoryResponse getInventoryByProductId(@PathVariable Long productId) {

        return inventoryService.getInventoryByProductId(productId);

    }

    // Endpoint pour mettre à jour l'inventaire
    @PutMapping("/update")
    public void updateInventory(@RequestBody UpdateInventoryRequest request) {
        try {
            inventoryService.updateInventory(request);
        } catch (Exception e) {
           System.out.println(e);
        }
    }
}
