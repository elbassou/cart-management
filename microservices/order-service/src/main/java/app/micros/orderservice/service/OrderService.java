package app.micros.orderservice.service;

import app.micros.orderservice.feignClient.CartClient;
import app.micros.orderservice.feignClient.InventoryClient;
import app.micros.orderservice.exception.OrderNotFoundException;
import app.micros.orderservice.model.Commande;
import app.micros.orderservice.model.OrderLine;
import app.micros.orderservice.repository.CommandeRepository;
import app.micros.orderservice.repository.OrderLineRepository;
import app.micros.shared.dto.OrderLineDTO;
import app.micros.shared.dto.OrderStatusDTO;
import app.micros.shared.request.UpdateInventoryRequest;
import app.micros.shared.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

   private  ObjectMapper objectMapper;
    private CommandeRepository orderRepository;
    private OrderLineRepository orderLineRepository;
    private InventoryClient inventoryClient;
    private CartClient cartClient;

    @Lazy
    public OrderService(CommandeRepository orderRepository,OrderLineRepository orderLineRepository,
        InventoryClient inventoryClient,@Autowired CartClient cartClient, ObjectMapper objectMapper){
    this.orderRepository = orderRepository;
    this.orderLineRepository = orderLineRepository;
    this.inventoryClient = inventoryClient;
    this.cartClient = cartClient;
    this.objectMapper = objectMapper;

    }

    @Transactional
    public void createOrder(Long clientId) {
        // Récupérer le panier pour ce client via le client Feign
        CartResponse cart = cartClient.getCartByClientId(clientId);

        // Gestion d'erreur : vérifier si le panier est vide ou inexistant
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Panier vide ou introuvable pour le client ID: " + clientId);
        }

        // Créer la commande
        Commande newOrder = new Commande();
        newOrder.setClientId(clientId);
        newOrder.setCartId(cart.getCartId());

        // Calculer le prix total de la commande
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newOrder.setTotalPrice(totalPrice);

        // Sauvegarder la commande pour obtenir un ID
        Commande savedOrder = orderRepository.save(newOrder);

        // Ajouter les articles de commande
        List<OrderLine> orderLines = cart.getItems().stream()
                .map(item -> {
                    OrderLine orderLine = new OrderLine(item.getProductId(), item.getQuantity(), item.getPrice());
                    orderLine.setCommandeId(savedOrder.getId()); // Associer à la commande
                    return orderLine;
                }).collect(Collectors.toList());

        // Sauvegarder les lignes de commande et collecter leurs IDs
        List<Long> orderLineIds = orderLineRepository.saveAll(orderLines).stream()
                .map(OrderLine::getId)
                .collect(Collectors.toList());

        // Mettre à jour la commande avec les IDs des lignes de commande
        savedOrder.setOrderLineIds(orderLineIds);
        orderRepository.save(savedOrder);

        // Ajuster le stock après la confirmation de la commande
        for (CartItemResponse item : cart.getItems()) {
            if(item.getProductId()!=null) {
                UpdateInventoryRequest updateInventoryRequest = new UpdateInventoryRequest(item.getProductId(), -item.getQuantity());
                inventoryClient.updateInventory(updateInventoryRequest);  // Appel à InventoryService
            }

        }
    }

    public List<OrderResponse> getClientOrderHistory(Long clientId) {
        List<Commande> commandes = orderRepository.findByClientId(clientId); // Méthode pour récupérer les commandes
        return commandes.stream()
                .map(commande -> new OrderResponse(
                        commande.getId(),
                        commande.getClientId(),
                        commande.getCommandeDate(),
                        OrderStatusDTO.valueOf(commande.getStatus().name()), // Convertir en OrderStatusDTO
                        commande.getTotalPrice()))
                .collect(Collectors.toList());
    }

    public OrderDetailsResponse getOrderDetails(Long orderId) {
        // Gestion d'erreur : si la commande n'existe pas, lancer une exception personnalisée
        Commande commande = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Commande introuvable : " + orderId));

        List<OrderLineDTO> orderLineDTOs = commande.getOrderLineIds().stream()
                .map(orderLineId -> {
                    // Récupération des détails de chaque ligne de commande
                    OrderLine orderLine = orderLineRepository.findById(orderLineId).orElse(null);
                    return new OrderLineDTO(orderLine.getProduitId(), orderLine.getQuantity(), orderLine.getPrice());
                })
                .collect(Collectors.toList());

        return new OrderDetailsResponse(
                commande.getId(),
                commande.getClientId(),
                commande.getCartId(),
                commande.getCommandeDate(),
                OrderStatusDTO.valueOf(commande.getStatus().name()), // Convertir en OrderStatusDTO
                commande.getTotalPrice(),
                orderLineDTOs);
    }

    // Mapper une ligne de commande à une réponse simplifiée
    private OrderLineResponse mapToOrderLineResponse(OrderLine orderLine) {

        return  objectMapper.convertValue(orderLine, OrderLineResponse.class);

    }


}
