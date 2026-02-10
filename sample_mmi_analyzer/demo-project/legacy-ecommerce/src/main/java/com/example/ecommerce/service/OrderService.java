package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order service with multiple design problems
 * Problem: Contains business logic that should be in domain
 * Problem: Cyclic dependency with InventoryService
 * Problem: Long methods with multiple responsibilities
 * Problem: Transaction boundaries not well defined
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PaymentService paymentService;

    /**
     * Problem: Method too long (should be broken down)
     * Problem: Business logic not in domain layer
     * Problem: No validation of invariants
     */
    @Transactional
    public Order createOrder(Long customerId, List<OrderItemRequest> itemRequests) {
        // Get customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create order
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setCustomerName(customer.getName());
        order.setCustomerEmail(customer.getEmail());
        order.setShippingAddress(customer.getAddress() + ", " + customer.getCity() + ", " + customer.getState());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // Problem: totalAmount calculated here instead of domain
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Save order first to get ID
        order = orderRepository.save(order);

        List<OrderItem> items = new ArrayList<>();

        // Process each item
        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Problem: Stock check logic here instead of domain service
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            // Problem: Calculation in service layer
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);

            orderItemRepository.save(orderItem);
            items.add(orderItem);

            // Update inventory (should be domain event)
            inventoryService.reduceStock(product.getId(), itemRequest.getQuantity());

            totalAmount = totalAmount.add(subtotal);
        }

        // Update total amount
        order.setTotalAmount(totalAmount);
        order.setItems(items);
        orderRepository.save(order);

        // Problem: Logging sensitive information
        System.out.println("Order created: " + order.getId() + " for customer: " + customer.getEmail());

        return order;
    }

    /**
     * Problem: No proper state transition validation
     */
    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Problem: Magic string comparison
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("Order cannot be confirmed");
        }

        // Problem: Status transition not encapsulated in domain
        order.setStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    /**
     * Problem: Should trigger payment through domain event
     */
    @Transactional
    public void processPayment(Long orderId, String cardNumber, String cardHolderName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Problem: Directly calling another service (tight coupling)
        paymentService.processPayment(orderId, order.getTotalAmount(), cardNumber, cardHolderName);

        order.setStatus("PAID");
        orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Problem: Method allows modification of confirmed orders
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Problem: No validation of state transitions
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    /**
     * Inner class for request DTO
     * Problem: Should be in separate package
     */
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
