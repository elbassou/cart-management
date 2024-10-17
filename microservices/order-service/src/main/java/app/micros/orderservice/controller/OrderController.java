package app.micros.orderservice.controller;


import app.micros.orderservice.repository.CommandeRepository;
import app.micros.orderservice.service.OrderService;
import app.micros.shared.request.CreateOrderRequest;
import app.micros.shared.response.CartResponse;
import app.micros.shared.response.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

public static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        try {
            orderService.createOrder(createOrderRequest.getClientId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Gérer les exceptions ici (par exemple, journaliser l'erreur)
            logger.info(" createOrder :  "+e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }

    @GetMapping("{clientId}")
    public  ResponseEntity<List<OrderResponse>>  getALlCommande(@PathVariable Long clientId) {

        try {
            List<OrderResponse>  list = orderService.getClientOrderHistory(clientId);

            return new ResponseEntity<List<OrderResponse>>(list, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}



