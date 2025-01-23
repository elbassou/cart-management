package app.micros.cartservice.controller;


import app.micros.cartservice.exception.CustomException;
import app.micros.cartservice.model.CartItem;
import app.micros.cartservice.service.CartService;
import app.micros.shared.dto.CartDTO;
import app.micros.shared.dto.ProductCartDTO;
import app.micros.shared.response.CartItemResponse;
import app.micros.shared.response.CartResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CartController.class)
public class CartControllerTestBis {

    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCartItems_Success() throws Exception {
        // Arrange
        Long customerId = 1L;
        CartResponse cartResponse = new CartResponse(); // Remplace par les données réelles de test.
        cartResponse.setItems(List.of(new CartItemResponse(1L, 2L, 2, BigDecimal.valueOf(100.0))));

        Mockito.when(cartService.getCartItems(customerId)).thenReturn(cartResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{customerId}/items", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists())
             //   .andExpect(jsonPath("$.items[0].name").value("Product 1"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].price").value(100.0));

        Mockito.verify(cartService, Mockito.times(1)).getCartItems(customerId);
    }

    @Test
    public void testGetCartItems_NotFound() throws Exception {
        // Arrange
        Long customerId = 2L;

        Mockito.when(cartService.getCartItems(customerId)).thenThrow(new RuntimeException("Cart not found"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{customerId}/items", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(cartService, Mockito.times(1)).getCartItems(customerId);
    }

    @Test
    public void testAddProductToCart_Success() throws Exception {
        // Arrange
        Long customerId = 1L;
        ProductCartDTO productCartDTO = new ProductCartDTO(100L, 2); // Entrée
        CartDTO cartDTO = new CartDTO(10L, List.of(1L, 2L)); // Résultat simulé

        Mockito.when(cartService.addToCart(customerId, productCartDTO.getProductId(), productCartDTO.getQuantity()))
                .thenReturn(cartDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{customerId}/items", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":100,\"quantity\":2}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Produit ajouté au panier avec succès."));

        Mockito.verify(cartService, Mockito.times(1)).addToCart(customerId, 100L, 2);
    }

    @Test
    public void testAddProductToCart_Failure() throws Exception {
        // Arrange
        Long customerId = 1L;
        ProductCartDTO productCartDTO = new ProductCartDTO(100L, 5); // Entrée
        String errorMessage = "Stock insuffisant pour le produit : 100";

        Mockito.doThrow(new CustomException(errorMessage))
                .when(cartService)
                .addToCart(customerId, productCartDTO.getProductId(), productCartDTO.getQuantity());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{customerId}/items", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":100,\"quantity\":5}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        Mockito.verify(cartService, Mockito.times(1)).addToCart(customerId, 100L, 5);
    }

}

