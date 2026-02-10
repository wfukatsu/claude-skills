package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order REST controller
 * Problem: Direct repository access bypassing service layer
 * Problem: No authentication/authorization
 * Problem: No input validation
 * Problem: No proper error handling
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;  // Problem: Controller accessing repository directly

    /**
     * Problem: No authentication check
     * Problem: No pagination for large result sets
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        // Problem: Bypassing service layer
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Problem: No request validation
     * Problem: Direct exposure of internal model
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        // Problem: No validation
        Order order = orderService.createOrder(request.getCustomerId(), request.getItems());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long id) {
        Order order = orderService.confirmOrder(id);
        return ResponseEntity.ok(order);
    }

    /**
     * CRITICAL: Accepts card number in request body (should use payment gateway token)
     */
    @PostMapping("/{id}/payment")
    public ResponseEntity<Void> processPayment(
            @PathVariable Long id,
            @RequestBody PaymentRequest request) {
        // CRITICAL: Handling raw card numbers
        orderService.processPayment(id, request.getCardNumber(), request.getCardHolderName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Problem: Allows arbitrary status changes
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Order order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }

    // Inner classes for request DTOs (should be separate)
    public static class CreateOrderRequest {
        private Long customerId;
        private List<OrderService.OrderItemRequest> items;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public List<OrderService.OrderItemRequest> getItems() {
            return items;
        }

        public void setItems(List<OrderService.OrderItemRequest> items) {
            this.items = items;
        }
    }

    public static class PaymentRequest {
        private String cardNumber;
        private String cardHolderName;

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardHolderName() {
            return cardHolderName;
        }

        public void setCardHolderName(String cardHolderName) {
            this.cardHolderName = cardHolderName;
        }
    }
}
