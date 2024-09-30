package app.micros.productservice.service;

import app.micros.productservice.model.Produit;
import app.micros.productservice.repository.ProductRepository;
import app.micros.shared.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {


   private ProductRepository productRepository;


    public ProductService( @Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    public ProductResponse getProductById(Long productId) {
        Produit produit = productRepository.getProductById(productId);

        if(produit==null)
            throw new RuntimeException("Product not found");
        ProductResponse   productResponse = new ProductResponse();
        productResponse.setId(productId);
        productResponse.setName(produit.getName());
        productResponse.setPrice(BigDecimal.valueOf(produit.getPrice() ));
        return   productResponse;

    }
}
