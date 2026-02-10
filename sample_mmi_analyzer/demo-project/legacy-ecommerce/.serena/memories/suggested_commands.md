# Suggested Commands - Legacy E-Commerce

## Build & Run Commands

### Build
```bash
mvn clean install              # Full build with tests
mvn clean install -DskipTests  # Build without tests
```

### Run Application
```bash
mvn spring-boot:run           # Start application on port 8080
```

### Testing
```bash
mvn test                      # Run all tests
mvn test -Dtest=ClassName     # Run specific test class
```

## Database Commands

### Start MySQL (Docker)
```bash
docker run -d \
  --name mysql-ecommerce \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=ecommerce \
  -p 3306:3306 \
  mysql:8.0
```

### Database Management
```bash
docker start mysql-ecommerce   # Start existing container
docker stop mysql-ecommerce    # Stop container
docker rm mysql-ecommerce      # Remove container
```

## API Testing
```bash
curl http://localhost:8080/api/orders      # List orders
curl http://localhost:8080/api/customers   # List customers
curl http://localhost:8080/api/products    # List products
```

## Setup Script
```bash
./setup.sh                    # Automated setup (MySQL + build)
```

## System-Specific Commands (macOS/Darwin)
```bash
ls -la                        # List files with details
find . -name "*.java"         # Find Java files
grep -r "pattern" src/        # Search in source files
open target/                  # Open directory in Finder
```

## Workshop Workflow Commands

### Phase 1: Analysis
```bash
claude /system-investigation  # Codebase structure analysis
claude /ddd-evaluation       # DDD principle evaluation
claude /mmi-evaluation       # Quality metrics
claude /evaluation-report    # Comprehensive report
```

### Phase 2: Design
```bash
claude /ddd-redesign         # Strategic & tactical design
claude /architecture-design  # Microservices architecture
claude /database-design      # ScalarDB schema design
claude /implementation-planning  # Task breakdown
```

### Phase 3: Reporting
```bash
claude /html-report-generator  # Generate HTML dashboard
open ../../reports/index.html  # View results
```
