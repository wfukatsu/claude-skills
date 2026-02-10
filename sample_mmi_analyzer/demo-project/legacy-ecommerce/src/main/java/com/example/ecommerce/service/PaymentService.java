package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Payment service
 * Problem: Stores sensitive card information
 * Problem: No integration with real payment gateway
 * Problem: Should be in separate bounded context
 */
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * CRITICAL: Stores card number in plain text
     * Problem: No PCI DSS compliance
     * Problem: Logs sensitive information
     */
    @Transactional
    public Payment processPayment(Long orderId, BigDecimal amount, String cardNumber, String cardHolderName) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPaymentMethod("CREDIT_CARD");

        // CRITICAL SECURITY ISSUE: Storing plain text card number
        payment.setCardNumber(cardNumber);
        payment.setCardHolderName(cardHolderName);
        payment.setAmount(amount);
        payment.setStatus("PROCESSING");

        // Simulate payment processing
        try {
            // Problem: Magic number (3 seconds)
            Thread.sleep(3000);

            // Problem: No actual payment gateway integration
            String transactionId = UUID.randomUUID().toString();
            payment.setTransactionId(transactionId);
            payment.setStatus("COMPLETED");

            // CRITICAL: Logging card number
            System.out.println("Payment processed for order " + orderId + " with card " + cardNumber);

        } catch (InterruptedException e) {
            payment.setStatus("FAILED");
            System.err.println("Payment processing failed: " + e.getMessage());
        }

        return paymentRepository.save(payment);
    }

    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
