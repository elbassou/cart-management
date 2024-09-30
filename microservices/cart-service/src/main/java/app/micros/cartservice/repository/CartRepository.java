package app.micros.cartservice.repository;

import app.micros.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByClientId(Long customerId);

    // Supprimer le panier par clientId
    void deleteByClientId(Long clientId);
}
