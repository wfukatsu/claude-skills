package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product REST controller
 * Problem: Direct repository access
 * Problem: No proper service layer usage
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;  // Problem: Direct repository access

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // Problem: Direct repository call from controller
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productRepository.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productRepository.save(product);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productData) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productData.getName());
        product.setDescription(productData.getDescription());
        product.setPrice(productData.getPrice());
        product.setCategory(productData.getCategory());

        Product updated = productRepository.save(product);
        return ResponseEntity.ok(updated);
    }

    /**
     * Problem: Direct inventory manipulation from controller
     */
    @PostMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        if (quantity > 0) {
            inventoryService.increaseStock(id, quantity);
        } else {
            inventoryService.reduceStock(id, Math.abs(quantity));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getStock(@PathVariable Long id) {
        Integer stock = inventoryService.getAvailableStock(id);
        return ResponseEntity.ok(stock);
    }
}
