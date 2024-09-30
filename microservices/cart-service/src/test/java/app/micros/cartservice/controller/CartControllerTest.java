package app.micros.cartservice.controller;

import app.micros.cartservice.model.Cart;
import app.micros.cartservice.service.CartService;
import app.micros.shared.dto.ProductCartDTO;
import app.micros.shared.response.CartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProductToCart_Success() {
        Long customerId = 1L;
        Long productId = 1L;
        int quantity = 2;
        ProductCartDTO productCartDTO = new ProductCartDTO( productId, quantity);

        ResponseEntity<String> response = cartController.addProductToCart(customerId, productCartDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Produit ajouté au panier avec succès.", response.getBody());

        verify(cartService, times(1)).addToCart(customerId, productId, quantity);
    }

    @Test
    void addProductToCart_Failure() {
        Long customerId = 1L;
        Long productId = 1L;
        int quantity = 2;
        ProductCartDTO productCartDTO = new ProductCartDTO( productId, quantity);
        doThrow(new RuntimeException("Erreur d'ajout")).when(cartService).addToCart(customerId, productId, quantity);

        ResponseEntity<String> response = cartController.addProductToCart(customerId, productCartDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erreur d'ajout", response.getBody());
    }

    @Test
    void deleteProductFromCart_Success() {
        Long customerId = 1L;
        Long productId = 1L;

        ResponseEntity<String> response = cartController.deleteProductFromCart(customerId, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Produit supprimé du panier avec succès.", response.getBody());

        verify(cartService, times(1)).deleteProductFromCart(customerId, productId);
    }

    @Test
    void deleteProductFromCart_Failure() {
        Long customerId = 1L;
        Long productId = 1L;

        doThrow(new RuntimeException("Produit non trouvé")).when(cartService).deleteProductFromCart(customerId, productId);

        ResponseEntity<String> response = cartController.deleteProductFromCart(customerId, productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produit non trouvé", response.getBody());
    }

    @Test
    void getCartItems_Success() {
        Long customerId = 1L;
        CartResponse mockCartResponse = new CartResponse();

        mockCartResponse.setCartId(1L);

        when(cartService.getCartItems(customerId)).thenReturn(mockCartResponse);

        ResponseEntity<CartResponse> response = cartController.getCartItems(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCartResponse, response.getBody());

        verify(cartService, times(1)).getCartItems(customerId);
    }

    @Test
    void getCartItems_Failure() {
        Long customerId = 1L;

        doThrow(new RuntimeException("Panier non trouvé")).when(cartService).getCartItems(customerId);

        ResponseEntity<CartResponse> response = cartController.getCartItems(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void confirmOrder_Success() {
        Long customerId = 1L;

        ResponseEntity<String> response = cartController.confirmOrder(customerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Commande confirmée avec succès.", response.getBody());

        verify(cartService, times(1)).confirmOrder(customerId);
    }

    @Test
    void confirmOrder_Failure() {
        Long customerId = 1L;

        doThrow(new RuntimeException("Erreur de confirmation")).when(cartService).confirmOrder(customerId);

        ResponseEntity<String> response = cartController.confirmOrder(customerId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erreur de confirmation", response.getBody());
    }

    @Test
    void getCartByClientId_Success() {
        Long clientId = 1L;
        CartResponse mockCartResponse = new CartResponse(); // Remplir avec les données nécessaires

        when(cartService.getCartItems(clientId)).thenReturn(mockCartResponse);

        ResponseEntity<CartResponse> response = cartController.getCartByClientId(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCartResponse, response.getBody());

        verify(cartService, times(1)).getCartItems(clientId);
    }
}
