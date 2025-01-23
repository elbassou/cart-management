package app.micros.cartservice.controller;

import app.micros.cartservice.service.CartService;
import app.micros.shared.dto.ProductCartDTO;
import app.micros.shared.response.CartResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Ajouter un produit au panier
    @PostMapping("/{customerId}/items")
    public ResponseEntity<String> addProductToCart(@PathVariable Long customerId,@RequestBody ProductCartDTO productCartDTO)
    {
        try {
            cartService.addToCart(customerId, productCartDTO.getProductId(), productCartDTO.getQuantity());
            return new ResponseEntity<>("Produit ajouté au panier avec succès.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer un produit du panier
    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long customerId,
                                                        @PathVariable Long productId) {
        try {
            cartService.deleteProductFromCart(customerId, productId);
            return new ResponseEntity<>("Produit supprimé du panier avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer les articles du panier
    @GetMapping("/{customerId}/items")
    public ResponseEntity<CartResponse> getCartItems(@PathVariable Long customerId) {
        try {
            CartResponse cartResponse = cartService.getCartItems(customerId);
            return new ResponseEntity<>(cartResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Confirmer la commande
    @PostMapping("/{customerId}/order")
    public ResponseEntity<String> confirmOrder(@PathVariable Long customerId) {
        try {
            cartService.confirmOrder(customerId);
            return new ResponseEntity<>("Commande confirmée avec succès.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<CartResponse> getCartByClientId(@PathVariable Long clientId) {
        CartResponse cartResponse = cartService.getCartItems(clientId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }
}
