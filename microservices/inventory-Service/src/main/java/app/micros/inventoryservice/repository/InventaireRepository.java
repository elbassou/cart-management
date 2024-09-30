package app.micros.inventoryservice.repository;

import app.micros.inventoryservice.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventaireRepository extends JpaRepository<Inventaire, Long> {
    Optional<Inventaire> findByProduitId(Long productId);
}
