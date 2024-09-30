package app.micros.inventoryservice.service;

import app.micros.inventoryservice.model.Inventaire;
import app.micros.inventoryservice.repository.InventaireRepository;
import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {


    private InventaireRepository inventaireRepository;

    public InventoryService (InventaireRepository inventaireRepository) {
       this.inventaireRepository = inventaireRepository;
    }

    // Récupérer l'inventaire par ID de produit
    public InventoryResponse getInventoryByProductId(Long productId) {
        Optional<Inventaire> inventaireOptional = inventaireRepository.findByProduitId(productId);
        if (inventaireOptional.isPresent()) {
            Inventaire inventaire = inventaireOptional.get();
            return new InventoryResponse(productId, inventaire.getQuantityAvailable());
        } else {
            throw new RuntimeException("Produit non trouvé : " + productId);
        }
    }

    // Mettre à jour l'inventaire
    public void updateInventory(UpdateInventoryRequest request) {
        Optional<Inventaire> inventaireOptional = inventaireRepository.findByProduitId(request.getProductId());
        if (inventaireOptional.isPresent()) {
            Inventaire inventaire = inventaireOptional.get();
            inventaire.setQuantityAvailable(inventaire.getQuantityAvailable() + request.getQuantityChange());
            inventaireRepository.save(inventaire);
        } else {
            throw new RuntimeException("Produit non trouvé : " + request.getProductId());
        }
    }
}
