#!/bin/bash

# Music School Management System - Build Script
# This script builds the application with Java 21 and prepares it for Docker deployment

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if Java 21 is available
check_java21() {
    print_status "Checking Java 21 availability..."
    
    if [ -d "/home/soroush/.jdks/graalvm-jdk-21.0.8" ]; then
        export JAVA_HOME="/home/soroush/.jdks/graalvm-jdk-21.0.8"
        export PATH="$JAVA_HOME/bin:$PATH"
        print_success "Java 21 found at $JAVA_HOME"
    else
        print_error "Java 21 not found. Please install GraalVM 21"
        exit 1
    fi
    
    # Verify Java version
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" != "21" ]; then
        print_error "Java version is not 21. Current version: $JAVA_VERSION"
        exit 1
    fi
    
    print_success "Java 21 is ready: $(java -version 2>&1 | head -n 1)"
}

# Function to clean previous builds
clean_build() {
    print_status "Cleaning previous builds..."
    mvn clean
    print_success "Clean completed"
}

# Function to run tests
run_tests() {
    print_status "Running tests..."
    if mvn test; then
        print_success "All tests passed"
    else
        print_error "Tests failed"
        exit 1
    fi
}

# Function to build the application
build_application() {
    print_status "Building application..."
    
    # Compile and package
    if mvn compile package -DskipTests=false; then
        print_success "Application built successfully"
    else
        print_error "Build failed"
        exit 1
    fi
}

# Function to prepare Docker files
prepare_docker() {
    print_status "Preparing Docker deployment files..."
    
    # Check if Dockerfile exists
    if [ ! -f "Dockerfile" ]; then
        print_warning "Dockerfile not found, creating a basic one..."
        cat > Dockerfile << 'EOF'
FROM openjdk:21-jre-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/music-school-management-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
        print_success "Dockerfile created"
    else
        print_success "Dockerfile already exists"
    fi
    
    # Check if docker-compose.yml exists
    if [ ! -f "docker-compose.yml" ]; then
        print_warning "docker-compose.yml not found, creating a basic one..."
        cat > docker-compose.yml << 'EOF'
version: '3.8'

services:
  music-school-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - postgres
    networks:
      - music-school-network

  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: musicschool
      POSTGRES_USER: musicschool
      POSTGRES_PASSWORD: musicschool123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - music-school-network

volumes:
  postgres_data:

networks:
  music-school-network:
    driver: bridge
EOF
        print_success "docker-compose.yml created"
    else
        print_success "docker-compose.yml already exists"
    fi
}

# Function to show build summary
show_summary() {
    print_status "Build Summary:"
    echo "=================="
    echo "✅ Java Version: $(java -version 2>&1 | head -n 1)"
    echo "✅ Application JAR: target/music-school-management-1.0.0.jar"
    echo "✅ Docker files: Dockerfile, docker-compose.yml"
    echo ""
    print_status "To run with Docker:"
    echo "  docker-compose up --build"
    echo ""
    print_status "To run directly:"
    echo "  java -jar target/music-school-management-1.0.0.jar"
    echo ""
    print_status "To run with Maven:"
    echo "  mvn spring-boot:run"
}

# Main execution
main() {
    echo "🎵 Music School Management System - Build Script"
    echo "================================================"
    echo ""
    
    # Check Java 21
    check_java21
    echo ""
    
    # Clean build
    clean_build
    echo ""
    
    # Run tests
    run_tests
    echo ""
    
    # Build application
    build_application
    echo ""
    
    # Prepare Docker
    prepare_docker
    echo ""
    
    # Show summary
    show_summary
    
    print_success "Build completed successfully! 🎉"
}

# Run main function
main "$@"
