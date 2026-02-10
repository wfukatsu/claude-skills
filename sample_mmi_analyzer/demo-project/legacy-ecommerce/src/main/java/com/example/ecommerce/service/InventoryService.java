package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Inventory service
 * Problem: Cyclic dependency with OrderService
 * Problem: Direct database manipulation without domain logic
 */
@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Problem: Direct stock modification without domain invariants
     * Problem: No inventory log creation
     * Problem: Race condition possible (optimistic locking not used)
     */
    @Transactional
    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Problem: No validation
        int newStock = product.getStockQuantity() - quantity;
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);

        // Problem: Logging to console instead of proper audit trail
        System.out.println("Stock reduced for product " + productId + ": " + quantity);
    }

    @Transactional
    public void increaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);
    }

    public Integer getAvailableStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return product.getStockQuantity();
    }
}
