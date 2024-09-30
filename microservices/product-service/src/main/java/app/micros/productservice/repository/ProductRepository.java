package app.micros.productservice.repository;

import app.micros.productservice.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Produit, Long> {

   Produit getProductById(Long productId);
}
