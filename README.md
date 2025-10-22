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

#### Option A: Using Build Scripts (Recommended)
```bash
# Full build with tests
./build.sh

# Quick build without tests (for development)
./quick-build.sh
```

#### Option B: Manual Maven Build
```bash
# Set Java 21
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Build application
mvn clean package
```

### 3. Run the Application

#### Option A: Using JAR file
```bash
java -jar target/music-school-management-1.0.0.jar
```

#### Option B: Using Maven
```bash
mvn spring-boot:run
```

#### Option C: Using Docker
```bash
# Build and run with Docker Compose
docker-compose up --build

# Or run in background
docker-compose up -d
```

### 4. Access the Application
- **Application**: http://localhost:8080
- **Grafana**: http://localhost:3000 (admin/admin123)
- **Prometheus**: http://localhost:9090
- **Database Admin**: http://localhost:8081

## 🔧 Development Setup

### Prerequisites
- **Java 21**: `sudo apt install openjdk-21-jdk`
- **Maven 3.9+**: `sudo apt install maven`
- **Docker**: `sudo apt install docker.io docker-compose`

### Local Development

#### 1. Database Setup
```bash
# Start PostgreSQL (for local development)
docker run -d --name postgres -p 5432:5432 -e POSTGRES_DB=musicschool -e POSTGRES_USER=musicschool -e POSTGRES_PASSWORD=musicschool123 postgres:15-alpine
```

#### 2. Build and Run
```bash
# Set Java 21
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Quick build and run
./quick-build.sh
mvn spring-boot:run
```

#### 3. Access
- **Application**: http://localhost:8080
- **API Docs**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

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

## 🛠️ Build Scripts

### Available Scripts

#### `build.sh` - Complete Build Process
```bash
./build.sh
```
**Features:**
- ✅ Java 21 verification and setup
- ✅ Clean previous builds
- ✅ Run all tests
- ✅ Package application
- ✅ Prepare Docker files
- ✅ Colored output with progress indicators

#### `quick-build.sh` - Fast Development Build
```bash
./quick-build.sh
```
**Features:**
- ⚡ Fast build without tests
- 🚀 Perfect for development
- 📦 Creates JAR file ready for deployment

### Build Output
After successful build:
- **JAR File**: `target/music-school-management-1.0.0.jar`
- **Docker Files**: `Dockerfile`, `docker-compose.yml`
- **Ready for**: Direct execution or Docker deployment

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

## 📚 Documentation

### Quick Start
- **[QUICK_START.md](docs/QUICK_START.md)**: Get started in 5 minutes
- **[BUILD_SCRIPTS.md](docs/BUILD_SCRIPTS.md)**: Detailed build scripts documentation
- **[DEPLOYMENT.md](docs/DEPLOYMENT.md)**: Complete deployment guide
- **[ARCHITECTURE.md](docs/ARCHITECTURE.md)**: Architecture overview
- **[API.md](docs/API.md)**: REST API documentation

### Project Information
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)**: Comprehensive project overview
- **[CHANGELOG.md](CHANGELOG.md)**: Version history and changes
- **[RELEASE_NOTES.md](RELEASE_NOTES.md)**: Release notes and features
- **[CONTRIBUTING.md](CONTRIBUTING.md)**: Contributing guidelines
- **[INDEX.md](docs/INDEX.md)**: Documentation index

### Build Scripts
- **`./build.sh`**: Complete build with tests and Docker preparation
- **`./quick-build.sh`**: Fast development build without tests

## 📝 API Documentation

### REST Endpoints
- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics
- `/swagger-ui.html` - API documentation