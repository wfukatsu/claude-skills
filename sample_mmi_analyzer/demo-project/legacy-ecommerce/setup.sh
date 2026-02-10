#!/bin/bash

# Setup script for Legacy E-Commerce demo project

echo "=========================================="
echo "Legacy E-Commerce System - Setup"
echo "=========================================="
echo ""

# Check Java version
echo "Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
echo "✅ Java $JAVA_VERSION detected"
echo ""

# Check Maven
echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.8 or higher."
    exit 1
fi
echo "✅ Maven detected"
echo ""

# Setup MySQL with Docker
echo "Setting up MySQL database..."
echo "Do you want to start MySQL using Docker? (y/n)"
read -r response

if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    if ! command -v docker &> /dev/null; then
        echo "❌ Docker is not installed."
        exit 1
    fi

    echo "Starting MySQL container..."
    docker run -d \
        --name mysql-ecommerce \
        -e MYSQL_ROOT_PASSWORD=root \
        -e MYSQL_DATABASE=ecommerce \
        -p 3306:3306 \
        mysql:8.0

    if [ $? -eq 0 ]; then
        echo "✅ MySQL container started successfully"
        echo "   Connection details:"
        echo "   Host: localhost"
        echo "   Port: 3306"
        echo "   Database: ecommerce"
        echo "   Username: root"
        echo "   Password: root"
        echo ""
        echo "Waiting for MySQL to be ready..."
        sleep 20
    else
        echo "⚠️  Failed to start MySQL container. It might already exist."
        echo "   You can stop it with: docker stop mysql-ecommerce"
        echo "   You can remove it with: docker rm mysql-ecommerce"
    fi
else
    echo "⚠️  Please ensure MySQL is running with these settings:"
    echo "   Host: localhost"
    echo "   Port: 3306"
    echo "   Database: ecommerce"
    echo "   Username: root"
    echo "   Password: root"
fi
echo ""

# Build the project
echo "Building the project..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Project built successfully"
else
    echo "❌ Build failed"
    exit 1
fi
echo ""

echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""
echo "To start the application, run:"
echo "  mvn spring-boot:run"
echo ""
echo "The API will be available at:"
echo "  http://localhost:8080/api"
echo ""
echo "To run the refactoring analysis:"
echo "  claude /system-investigation"
echo "  claude /ddd-evaluation"
echo "  claude /mmi-evaluation"
echo ""
