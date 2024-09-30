package app.micros.cartservice.service;

import app.micros.cartservice.exception.CustomException;
import app.micros.cartservice.feignClient.InventoryClient;
import app.micros.cartservice.feignClient.OrderClient;
import app.micros.cartservice.feignClient.ProductClient;
import app.micros.cartservice.model.Cart;
import app.micros.cartservice.model.CartItem;
import app.micros.cartservice.repository.CartItemRepository;
import app.micros.cartservice.repository.CartRepository;
import app.micros.shared.dto.CartDTO;
import app.micros.shared.request.CreateOrderRequest;
import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.CartItemResponse;
import app.micros.shared.response.CartResponse;
import app.micros.shared.response.InventoryResponse;
import app.micros.shared.response.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private CartRepository cartRepository;
    private ProductClient productClient;
    private InventoryClient inventoryClient;
    private CartItemRepository cartItemRepository;
    private OrderClient orderClient;

    public CartService(CartRepository cartRepository,ProductClient productClient,InventoryClient inventoryClient,
                       CartItemRepository cartItemRepository,OrderClient orderClient) {
        this.cartRepository = cartRepository;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
        this.cartItemRepository = cartItemRepository;
        this.orderClient = orderClient;

    }

    public CartDTO getCartFromCache(Long clientId) {
        // Récupérer ou créer un nouveau panier depuis la base de données
        Cart cart = cartRepository.findByClientId(clientId)
                .orElse(new Cart(clientId));

        // Convertir en DTO avant de retourner
        return new CartDTO(cart.getId(), cart.getCartItemIds());
    }


    @Transactional
    public CartDTO addToCart(Long clientId, Long productId, int quantity) {
        // Récupérer le produit
        ProductResponse product = productClient.getProductById(productId);

        // Récupérer ou créer un panier pour le client
        Cart cart = cartRepository.findByClientId(clientId)
                .orElse(new Cart(clientId));

        // Vérifier le stock
        InventoryResponse inventory = inventoryClient.getInventoryByProductId(productId);
        if (inventory.getQuantityInStock() < quantity) {
            throw new CustomException("Stock insuffisant pour le produit : " + productId);
        }

        // Ajouter ou mettre à jour l'article dans le panier
        Optional<CartItem> existingItemOpt = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cartItemRepository.save(newItem);
            cart.getCartItemIds().add(newItem.getId());
        }

        // Sauvegarder le panier et mettre à jour le stock
        cartRepository.save(cart);
        UpdateInventoryRequest updateRequest = new UpdateInventoryRequest(productId, -quantity);
        inventoryClient.updateInventory(updateRequest);

        // Retourner le panier mis à jour
        return new CartDTO(cart.getId(), cart.getCartItemIds());
    }


    @Transactional
    public CartDTO deleteProductFromCart(Long clientId, Long productId) {
        // Récupérer le panier
        Cart cart = cartRepository.findByClientId(clientId)
                .orElseThrow(() -> new CustomException("Panier introuvable pour le client: " + clientId));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId())
                .orElseThrow(() -> new CustomException("Produit non présent dans le panier: " + productId));

        // Supprimer le produit du panier
        cart.getCartItemIds().remove(cartItem.getId());
        int quantityRemoved = cartItem.getQuantity();
        cartItemRepository.delete(cartItem);

        // Sauvegarder le panier et remettre la quantité dans l'inventaire
        cartRepository.save(cart);
        UpdateInventoryRequest updateRequest = new UpdateInventoryRequest(productId, quantityRemoved);
        inventoryClient.updateInventory(updateRequest);

        return new CartDTO(cart.getId(), cart.getCartItemIds());
    }

    // 4. Confirmer la commande et vider le cache Redis
    @Transactional
    public void confirmOrder(Long clientId) {
        // Récupérer les articles du panier
        CartResponse cartResponse = getCartItems(clientId);

        // Créer une commande
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(clientId, cartResponse.getItems());
        orderClient.createOrder(createOrderRequest);

        // Supprimer tous les articles du panier
        cartItemRepository.deleteAllByCartId(cartResponse.getCartId());
        cartRepository.deleteByClientId(clientId);
    }

    // Méthode pour récupérer les articles du panier (sans cache ici)
    public CartResponse getCartItems(Long clientId) {
        logger.info("Fetching cart for clientId: " + clientId + " from DB");
        Cart cart = cartRepository.findByClientId(clientId)
                .orElseThrow(() -> new CustomException("Panier introuvable pour le client : " + clientId));

        List<CartItem> items = cartItemRepository.findAllById(cart.getCartItemIds());
        List<CartItemResponse> itemResponses = items.stream()
                .map(item -> new CartItemResponse(item.getId(), item.getProductId(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList());

        return new CartResponse(cart.getId(), itemResponses);
    }
}
