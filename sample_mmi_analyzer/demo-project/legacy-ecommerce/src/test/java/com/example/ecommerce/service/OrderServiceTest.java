package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Order service test
 * Problem: Low test coverage (only basic happy path)
 * Problem: No edge case testing
 * Problem: No integration tests
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private OrderService orderService;

    /**
     * Problem: Only tests happy path
     */
    @Test
    void testCreateOrder_Success() {
        // Setup
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStockQuantity(10);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        List<OrderService.OrderItemRequest> items = new ArrayList<>();
        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(1L);
        item.setQuantity(2);
        items.add(item);

        // Execute
        Order order = orderService.createOrder(customerId, items);

        // Verify
        assertNotNull(order);
        assertEquals("PENDING", order.getStatus());
        verify(orderRepository, atLeastOnce()).save(any(Order.class));
    }

    // Problem: Missing tests for:
    // - Customer not found
    // - Product not found
    // - Insufficient stock
    // - Invalid quantities
    // - Concurrent order creation
    // - Transaction rollback scenarios
}
