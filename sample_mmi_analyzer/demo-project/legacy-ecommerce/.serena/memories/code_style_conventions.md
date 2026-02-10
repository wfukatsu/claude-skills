# Code Style & Conventions

## Java Conventions
- **Java Version**: Java 17
- **Encoding**: UTF-8
- **Package Structure**: `com.example.ecommerce.*`

## Naming Conventions
- **Classes**: PascalCase (e.g., `OrderController`, `CustomerService`)
- **Methods**: camelCase (e.g., `createOrder`, `validatePayment`)
- **Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)

## Spring Boot Patterns
- **Controllers**: Annotated with `@RestController`, endpoints use `@RequestMapping`
- **Services**: Annotated with `@Service`, contain business logic
- **Repositories**: Extend `JpaRepository<Entity, ID>`
- **Entities**: Annotated with `@Entity`, use JPA annotations

## Lombok Usage
- `@Data`: Generates getters, setters, toString, equals, hashCode
- `@AllArgsConstructor`: Generates constructor with all fields
- `@NoArgsConstructor`: Generates no-args constructor (required for JPA)

## Layer Structure (Current - has violations)
```
Controller → Service → Repository → Database
```

Note: Current code has layer violations (controllers accessing repositories directly) - this is intentional for workshop demonstration.

## Testing Conventions (Limited)
- Test classes: `*Test.java` in `src/test/java`
- Uses JUnit 5 and Spring Boot Test
- H2 in-memory database for tests

## Documentation Style
- **Javadoc**: Minimal (intentionally lacking for workshop)
- **Comments**: Sparse (intentional technical debt)
- **README**: Japanese with some English technical terms
