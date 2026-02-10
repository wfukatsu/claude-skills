# Legacy E-Commerce System - Project Overview

## Purpose
This is a **demo project for Claude Code refactoring agent workshop**. It contains intentional technical debt and design problems for educational purposes. This is NOT production code.

**Important**: Do NOT fix the intentional flaws - they are pedagogical examples for the workshop.

## Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.0
- **ORM**: Spring Data JPA
- **Database**: MySQL 8.0
- **Build Tool**: Maven 3.8+
- **Libraries**: Lombok (boilerplate reduction)

## Domain Areas
1. **Order Management**: Processing customer orders
2. **Inventory Management**: Product stock management
3. **Customer Management**: Customer information registration/updates
4. **Payment Processing**: Credit card payment processing

## Project Structure
```
src/main/java/com/example/ecommerce/
├── controller/          # REST API endpoints
│   ├── OrderController.java
│   ├── CustomerController.java
│   └── ProductController.java
├── service/             # Business logic (with issues)
│   ├── OrderService.java
│   ├── InventoryService.java
│   ├── CustomerService.java
│   └── PaymentService.java
├── model/               # JPA entities
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Customer.java
│   ├── Product.java
│   └── Payment.java
├── repository/          # Data access layer
│   ├── OrderRepository.java
│   ├── CustomerRepository.java
│   ├── ProductRepository.java
│   ├── OrderItemRepository.java
│   └── PaymentRepository.java
└── util/                # Utilities
    ├── DateUtil.java
    └── ValidationUtil.java
```

## Known Issues (Intentional)
- Unclear bounded contexts (all domains in single package)
- No entity/value object distinction
- Improper aggregate boundaries
- Layer violations (controllers accessing repositories directly)
- Circular dependencies (OrderService ⇄ InventoryService)
- Security problems (plaintext credit card storage)
- Test coverage <20%
- 100+ line methods
- Missing documentation
