package app.micros.cartservice.repository;

import app.micros.cartservice.model.CartItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("toto-titi")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void findByProductIdAndCartId_ShouldReturnItem_WhenItemExists() {
        // Préparer les données
        CartItem cartItem = new CartItem();
        cartItem.setCartId(1L);
        cartItem.setProductId(101L);
        cartItem.setQuantity(2);
        cartItem.setPrice(BigDecimal.TEN);
        cartItemRepository.save(cartItem);

        // Exécuter la méthode
        Optional<CartItem> result = cartItemRepository.findByProductIdAndCartId(101L, 1L);

        // Vérifier le résultat
        assertTrue(result.isPresent());
        assertEquals(101L, result.get().getProductId());
        assertEquals(1L, result.get().getCartId());
    }

    @Test
    void findAllByCartId_ShouldReturnItems_WhenItemsExist() {
        // Préparer les données
        CartItem item1 = new CartItem();
        item1.setCartId(1L);
        item1.setProductId(101L);
        item1.setQuantity(2);
        item1.setPrice(BigDecimal.TEN);

        CartItem item2 = new CartItem();
        item2.setCartId(1L);
        item2.setProductId(102L);
        item2.setQuantity(1);
        item2.setPrice(BigDecimal.TEN);
        cartItemRepository.saveAll(List.of(item1, item2));

        // Exécuter la méthode
        List<CartItem> result = cartItemRepository.findAllByCartId(1L);

        // Vérifier le résultat
        assertEquals(2, result.size());
    }

    @Test
    void deleteAllByCartId_ShouldDeleteItems_WhenItemsExist() {
        // Préparer les données
        CartItem item1 = new CartItem();
        item1.setCartId(1L);
        item1.setProductId(101L);
        item1.setPrice(BigDecimal.valueOf(122));
        cartItemRepository.save(item1);

        // Supprimer
        cartItemRepository.deleteAllByCartId(1L);

        // Vérifier que les articles ont été supprimés
        List<CartItem> result = cartItemRepository.findAllByCartId(1L);
        assertTrue(result.isEmpty());
    }
}

