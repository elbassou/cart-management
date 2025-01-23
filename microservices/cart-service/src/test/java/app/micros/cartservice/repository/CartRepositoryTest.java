package app.micros.cartservice.repository;

import app.micros.cartservice.model.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void findByClientId_ShouldReturnCart_WhenCartExists() {
        // Préparer les données
        Cart cart = new Cart(1L);
        cartRepository.save(cart);

        // Exécuter la méthode
        Optional<Cart> result = cartRepository.findByClientId(1L);

        // Vérifier le résultat
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getClientId());
    }

    @Test
    void findByClientId_ShouldReturnEmpty_WhenCartDoesNotExist() {
        // Exécuter la méthode
        Optional<Cart> result = cartRepository.findByClientId(99L);

        // Vérifier le résultat
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteByClientId_ShouldDeleteCart_WhenCartExists() {
        // Préparer les données
        Cart cart = new Cart(1L);
        cartRepository.save(cart);

        // Supprimer
        cartRepository.deleteByClientId(1L);

        // Vérifier que le panier a été supprimé
        Optional<Cart> result = cartRepository.findByClientId(1L);
        assertTrue(result.isEmpty());
    }
}

