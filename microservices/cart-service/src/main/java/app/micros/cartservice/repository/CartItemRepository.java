package app.micros.cartservice.repository;

import app.micros.cartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);
    // Récupérer tous les CartItems pour un panier spécifique
    List<CartItem> findAllByCartId(Long cartId);

    // Supprimer tous les CartItems associés à un panier
    void deleteAllByCartId(Long cartId);
}
