package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity
 * Problem: Not an aggregate root (items can be modified independently)
 * Problem: Missing invariant enforcement
 * Problem: Status should be value object/enum
 * Problem: Total amount is mutable after order confirmation
 */
@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Problem: Should maintain reference, not duplicate data
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;

    private LocalDateTime orderDate;

    // Problem: Should be enum or value object
    private String status;

    // Problem: Should be immutable after confirmation
    private BigDecimal totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Problem: No cascade operations defined for aggregate
    @OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
