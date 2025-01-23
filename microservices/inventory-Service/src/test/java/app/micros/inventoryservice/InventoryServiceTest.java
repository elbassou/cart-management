package app.micros.inventoryservice;


import app.micros.inventoryservice.model.Inventaire;
import app.micros.inventoryservice.repository.InventaireRepository;
import app.micros.inventoryservice.service.InventoryService;
import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.InventoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private InventaireRepository inventaireRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetInventoryByProductId_ProductFound() {
        // Arrange
        Long productId = 1L;
        Inventaire inventaire = new Inventaire();
        inventaire.setProduitId(productId);
        inventaire.setQuantityAvailable(100);

        when(inventaireRepository.findByProduitId(productId)).thenReturn(Optional.of(inventaire));

        // Act
        InventoryResponse response = inventoryService.getInventoryByProductId(productId);

        // Assert
        assertNotNull(response);
        assertEquals(productId, response.getProductId());
        assertEquals(100, response.getQuantityInStock());
        verify(inventaireRepository, times(1)).findByProduitId(productId);
    }

    @Test
    public void testGetInventoryByProductId_ProductNotFound() {
        // Arrange
        Long productId = 1L;
        when(inventaireRepository.findByProduitId(productId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.getInventoryByProductId(productId);
        });

        assertEquals("Produit non trouvé : " + productId, exception.getMessage());
        verify(inventaireRepository, times(1)).findByProduitId(productId);
    }

    @Test
    public void testUpdateInventory_ProductFound() {
        // Arrange
        Long productId = 1L;
        int quantityChange = 50;
        Inventaire inventaire = new Inventaire();
        inventaire.setProduitId(productId);
        inventaire.setQuantityAvailable(100);

        UpdateInventoryRequest request = new UpdateInventoryRequest(productId, quantityChange);

        when(inventaireRepository.findByProduitId(productId)).thenReturn(Optional.of(inventaire));

        // Act
        inventoryService.updateInventory(request);

        // Assert
        assertEquals(150, inventaire.getQuantityAvailable());
        verify(inventaireRepository, times(1)).findByProduitId(productId);
        verify(inventaireRepository, times(1)).save(inventaire);
    }

    @Test
    public void testUpdateInventory_ProductNotFound() {
        // Arrange
        Long productId = 1L;
        int quantityChange = 50;
        UpdateInventoryRequest request = new UpdateInventoryRequest(productId, quantityChange);

        when(inventaireRepository.findByProduitId(productId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.updateInventory(request);
        });

        assertEquals("Produit non trouvé : " + productId, exception.getMessage());
        verify(inventaireRepository, times(1)).findByProduitId(productId);
        verify(inventaireRepository, never()).save(any(Inventaire.class));
    }
}
