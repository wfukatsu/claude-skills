package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order item entity
 * Problem: Can be created/modified independently of Order (weak aggregate)
 * Problem: Denormalizes product information
 * Problem: No invariant enforcement (quantity, price validation)
 */
@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Problem: Should be part of Order aggregate
    private Long orderId;

    private Long productId;

    // Problem: Denormalized data
    private String productName;

    private Integer quantity;
    private BigDecimal unitPrice;

    // Problem: Calculated field stored redundantly
    private BigDecimal subtotal;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Problem: Calculation logic in entity
        if (subtotal == null && quantity != null && unitPrice != null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
