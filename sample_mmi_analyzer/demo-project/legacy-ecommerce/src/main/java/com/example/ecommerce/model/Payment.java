package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment entity
 * Problem: Stores sensitive card information in plain text
 * Problem: Should be in separate bounded context (Payment)
 * Problem: Status should be enum
 */
@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String paymentMethod;

    // CRITICAL SECURITY ISSUE: Storing card number in plain text
    private String cardNumber;
    private String cardHolderName;

    private BigDecimal amount;

    // Problem: Should be enum
    private String status;

    private String transactionId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }
}
