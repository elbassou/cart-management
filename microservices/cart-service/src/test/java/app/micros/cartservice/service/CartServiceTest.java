package app.micros.cartservice.service;

import app.micros.cartservice.exception.CustomException;
import app.micros.cartservice.feignClient.InventoryClient;
import app.micros.cartservice.feignClient.ProductClient;
import app.micros.cartservice.model.Cart;
import app.micros.cartservice.model.CartItem;
import app.micros.cartservice.repository.CartItemRepository;
import app.micros.cartservice.repository.CartRepository;
import app.micros.shared.dto.CartDTO;
import app.micros.shared.response.InventoryResponse;
import app.micros.shared.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddToCart_Success() {
        Long customerId = 1L;
        Long productId = 2L;
        int quantity = 3;

        // Simuler la réponse du produit
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(productId);
        productResponse.setPrice(new BigDecimal("100.0"));
        when(productClient.getProductById(productId)).thenReturn(productResponse);

        // Simuler la réponse de l'inventaire
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setProductId(productId);
        inventoryResponse.setQuantityInStock(10);
        when(inventoryClient.getInventoryByProductId(productId)).thenReturn(inventoryResponse);

        // Simuler un panier existant
        Cart cart = new Cart(customerId);
        when(cartRepository.findByClientId(customerId)).thenReturn(Optional.of(cart));

        // Simuler l'absence d'item existant dans le panier
        when(cartItemRepository.findByProductIdAndCartId(productId, cart.getId())).thenReturn(Optional.empty());

        // Appeler la méthode addToCart
        CartDTO result = cartService.addToCart(customerId, productId, quantity);

        // Vérifier que les méthodes correctes ont été appelées
        verify(cartRepository, times(1)).findByClientId(customerId);
        verify(productClient, times(1)).getProductById(productId);
        verify(inventoryClient, times(1)).getInventoryByProductId(productId);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(any(Cart.class));

        // Vérifier les valeurs du retour
        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        assertEquals(1, result.getCartItemIds().size());
    }

    @Test
    public void testAddToCart_InsufficientStock() {
        Long customerId = 1L;
        Long productId = 2L;
        int quantity = 10;

        // Simuler la réponse du produit
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(productId);
        productResponse.setPrice(new BigDecimal("100.0"));
        when(productClient.getProductById(productId)).thenReturn(productResponse);

        // Simuler une quantité insuffisante dans l'inventaire
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setProductId(productId);
        inventoryResponse.setQuantityInStock(5);  // Stock insuffisant
        when(inventoryClient.getInventoryByProductId(productId)).thenReturn(inventoryResponse);

        // Simuler un panier existant
        Cart cart = new Cart(customerId);
        when(cartRepository.findByClientId(customerId)).thenReturn(Optional.of(cart));

        // Vérifier que l'exception est lancée
        CustomException exception = assertThrows(CustomException.class, () -> {
            cartService.addToCart(customerId, productId, quantity);
        });

        assertEquals("Stock insuffisant pour le produit : " + productId, exception.getMessage());

        // Vérifier que les méthodes correctes ont été appelées
        verify(cartRepository, times(1)).findByClientId(customerId);
        verify(productClient, times(1)).getProductById(productId);
        verify(inventoryClient, times(1)).getInventoryByProductId(productId);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }
}
