package app.micros.cartservice.controller;

import app.micros.shared.dto.ProductCartDTO;
import app.micros.shared.response.CartItemResponse;
import app.micros.shared.response.CartResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * If we intend to write an integration test where no mocks are needed, the
 * @AutoConfigureMockMvc can be used with @SpringBootTest to bootstrap the full
 * application context. When this annotation is used, the mockMvc object is created and
 * configured for you automatically. You just need to autowire it in the test class to send
 * requests to the application
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // Utilisé pour convertir les objets Java en JSON et vice versa.

    @BeforeEach
    public void setUp() {
        // Configurer l'état avant chaque test si nécessaire (par exemple, réinitialiser des données ou configurer des mocks).
    }

    // Test pour la méthode GET /api/cart/{customerId}/items
    //@Test
    public void testGetCartItems_Success() throws Exception {
        // Arrange


        Long customerId = 1L;
        CartResponse cartResponse = new CartResponse();  // Créez un CartResponse valide à tester
        cartResponse.setCartId(1L);
        cartResponse.setItems(List.of(new CartItemResponse(1L, 2), new CartItemResponse(2L, 1)));  // Exemple d'articles dans le panier

        // Ici, on suppose que cartService.getCartItems() retourne cartResponse
        // Vous pouvez utiliser des mockBeans dans votre configuration de test si vous n'avez pas de base de données réelle

        mockMvc.perform(get("/api/cart/{customerId}/items", customerId))
                .andExpect(status().isOk())  // Le code HTTP attendu est 200 OK
                .andExpect(jsonPath("$.cartId").value(1L))  // Vérifier que le cartId est correct
                .andExpect(jsonPath("$.items[0].productId").value(1L))  // Vérifier les éléments
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    // Test pour la méthode POST /api/cart/{customerId}/items
    @Test
    //@Order(1)
    public void testAddProductToCart_Success() throws Exception {
        // Arrange
        Long customerId = 1L;
        ProductCartDTO productCartDTO = new ProductCartDTO(1L, 2);  // Un produit à ajouter avec quantité
        String jsonRequest = objectMapper.writeValueAsString(productCartDTO);  // Convertir en JSON

        // Act & Assert
        mockMvc.perform(post("/api/cart/{customerId}/items", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))  // Envoyer les données dans le corps de la requête
                .andExpect(status().isCreated())  // Le code HTTP attendu est 201 Created
                .andExpect(content().string("Produit ajouté au panier avec succès."));  // Vérifier la réponse

        // Si vous utilisez des mocks pour cartService, vérifiez qu'ils ont bien été appelés
        // Vous pouvez aussi vérifier que l'état du panier est bien modifié (en utilisant des mockBeans ou des assertions sur des entités persistées)
    }

    // Test pour la méthode POST avec gestion d'erreur (par exemple, un stock insuffisant)
    @Test
    public void testAddProductToCart_Failure() throws Exception {
        // Arrange
        Long customerId = 1L;
        ProductCartDTO productCartDTO = new ProductCartDTO(1L, 3000);  // Tenter d'ajouter plus de produits que disponibles
        String jsonRequest = objectMapper.writeValueAsString(productCartDTO);  // Convertir en JSON

        // On s'attend à ce que le service lève une exception de type CustomException
        // Nous devons tester le cas où le service renvoie une erreur de stock insuffisant
        String errorMessage = "Stock insuffisant pour le produit : 1";

        mockMvc.perform(post("/api/cart/{customerId}/items", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())  // Code HTTP attendu : 400 Bad Request
                .andExpect(content().string(errorMessage));  // Vérifier que le message d'erreur est bien retourné
    }
}
