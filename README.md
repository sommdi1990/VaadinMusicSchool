# 🎵 Music School Management System

A comprehensive music school management system built with Java 21, Spring Boot, Vaadin, and CockroachDB, featuring modern web UI, monitoring, and Docker containerization.

## 🚀 Features

### Core Functionality
- **Database Migrations**: Version-controlled schema migrations with Flyway [Updated for Flyway]
- **Student Management**: Complete student registration, profiles, and enrollment tracking
- **Instructor Management**: Instructor profiles, specializations, and course assignments
- **Course Management**: Course creation, scheduling, and capacity management
- **Enrollment System**: Student-course enrollment with payment tracking
- **Dashboard**: Real-time statistics and analytics
- **Reports**: Comprehensive reporting and analytics

### Technical Features
- **Flyway Migration**: Manage SQL schema changes with versioned migrations—repeatable and reviewable [Updated for Flyway]
- **Modern UI**: Built with Vaadin 24.9.3 for responsive, accessible web interface
- **Java 21**: Latest LTS version with modern language features
- **Spring Boot 3.2.0**: Enterprise-grade application framework
- **CockroachDB**: Distributed SQL database for scalability
- **Docker Support**: Multi-stage builds and container orchestration
- **Monitoring**: Prometheus metrics and Grafana dashboards
- **Security**: Built-in authentication and authorization

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Vaadin UI     │    │  Spring Boot    │    │  CockroachDB    │
│   (Frontend)    │◄──►│   (Backend)     │◄──►│   (Database)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Grafana       │    │   Prometheus    │    │   Monitoring    │
│ (Visualization) │◄──►│   (Metrics)     │◄──►│   (Health)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Vaadin
```

### 2. Build the Application
```bash
mvn clean package -DskipTests
```

### 2. Database Migration (Flyway)
Run database migrations with Flyway before application startup:
```bash
docker-compose up flyway
```
یا برای اجرای همزمان با سایر سرویس‌ها:
```bash
docker-compose up
```
Flyway به صورت خودکار اسکریپت‌های  `src/main/resources/db/migration`  را اجرا می‌کند. [Updated for Flyway]

### 3. Run with Docker Compose
```bash
docker-compose up -d
```

### 4. Access the Application
- **Application**: http://localhost:8080/music-school
- **Grafana**: http://localhost:3000 (admin/admin123)
- **Prometheus**: http://localhost:9090
- **CockroachDB Admin**: http://localhost:8081

## 🔧 Development Setup

### Local Development
1. **Start CockroachDB**:
   ```bash
   docker run -d --name cockroachdb -p 26257:26257 -p 8081:8080 cockroachdb/cockroach:v23.1.10 start-single-node --insecure --http-addr=0.0.0.0:8080
   ```

2. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Access**: http://localhost:8080/music-school

### Database Setup
The application will automatically create the database schema on first run. The default configuration connects to:
- **Host**: localhost:26257
- **Database**: musicschool
- **Username**: root
- **Password**: (empty)

## 📊 Monitoring

### Prometheus Metrics
The application exposes metrics at `/music-school/actuator/prometheus`:
- JVM metrics (memory, GC, threads)
- HTTP request metrics
- Database connection pool metrics
- Custom business metrics

### Grafana Dashboards
Pre-configured dashboards include:
- Application health monitoring
- JVM performance metrics
- HTTP request patterns
- Database connection monitoring

## 🗄️ Database Schema

**Database migrations are managed by Flyway. Schema is NOT created automatically; use migration scripts in `src/main/resources/db/migration/` to modify structure.** [Updated for Flyway]

### Core Entities
- **Students**: Personal information, contact details, skill level
- **Instructors**: Professional profiles, specializations, rates
- **Courses**: Course details, scheduling, capacity
- **Enrollments**: Student-course relationships, payments

### Key Relationships
- Students can enroll in multiple courses
- Instructors can teach multiple courses
- Each enrollment tracks payment status
- Audit fields track creation and modification times

## 🎨 UI Components

### Main Views
- **Dashboard**: Overview with key metrics and charts
- **Students**: Student management with search and CRUD operations
- **Instructors**: Instructor management and profiles
- **Courses**: Course catalog and scheduling
- **Enrollments**: Enrollment management and payment tracking

### UI Features
- Responsive design for all screen sizes
- Accessible components (WCAG 2.1 AA compliant)
- Modern Material Design principles
- Real-time data updates
- Advanced filtering and search

## 🔒 Security

### Authentication
- Basic authentication with configurable credentials
- Session management with automatic timeout
- CSRF protection enabled

### Authorization
- Role-based access control
- Secure API endpoints
- Input validation and sanitization

## 📈 Performance

### Optimization Features
- Connection pooling with HikariCP
- Lazy loading for large datasets
- Pagination for data tables
- Caching for frequently accessed data

### Monitoring
- Application performance metrics
- Database query monitoring
- Memory and CPU usage tracking
- Custom business metrics

## 🐳 Docker Configuration

### Multi-stage Build
- **Build Stage**: Maven compilation and packaging
- **Runtime Stage**: Optimized JRE image with application

### Container Features
- Health checks for all services
- Resource limits and optimization
- Security best practices
- Logging configuration

### Database Migration (Flyway)
A dedicated Flyway service automatically applies migration scripts:
```yaml
  flyway:
    image: flyway/flyway:10
    command: -url=jdbc:postgresql://db:26257/musicschool?sslmode=require -user=musicschool -password=musicschool123 -connectRetries=20 migrate
    volumes:
      - ./src/main/resources/db/migration:/flyway/sql
    depends_on:
      - db
    restart: on-failure
```
You can **run just the migrations** with: `docker-compose up flyway` or have it run alongside application startup. [Updated for Flyway]

## 📝 API Documentation

### REST Endpoints
- `/music-school/actuator/health` - Health check
- `/music-school/actuator/metrics` - Application metrics
- `/music-school/actuator/prometheus`