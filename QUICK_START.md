# 🚀 Quick Start Guide

## Get Started in 5 Minutes

This guide will help you get the Music School Management System up and running quickly.

## Prerequisites

- **Java 21**: `sudo apt install openjdk-21-jdk`
- **Maven 3.9+**: `sudo apt install maven`
- **Docker**: `sudo apt install docker.io docker-compose`
- **Git**: `sudo apt install git`

## 🎯 Option 1: Using Docker Compose (Recommended)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Vaadin
```

### 2. Start All Services

```bash
# Start all services with Docker Compose
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f
```

### 3. Access the Application

- **Application**: http://localhost:8080/music-school/
- **Database Admin**: http://localhost:8081
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Mock Email Server**: http://localhost:3001

## 🔐 Default Credentials

### Admin User

- **Username**: `admin`
- **Email**: `admin@musicschool.com`
- **Password**: `admin123`
- **Role**: ADMIN

### Sample Users

- **Instructor**: `instructor1` / `instructor1@musicschool.com` / `admin123`
- **Student**: `student1` / `student1@musicschool.com` / `admin123`

### Sample Data

The system comes pre-loaded with:

- 6 instructors with different specializations
- 5 students with various skill levels
- 6 courses covering different instruments
- Sample enrollments and schedules
- Payment records and audit logs

## 🎯 Option 2: Using Build Scripts

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Vaadin
```

### 2. Run the Build Script
```bash
# Full build with tests and Docker preparation
./build.sh
```

### 3. Start the Application
```bash
# Run the JAR file
java -jar target/music-school-management-1.0.0.jar

# Or run with Maven
mvn spring-boot:run
```

### 4. Access the Application

- **Application**: http://localhost:8080/music-school/
- **Health Check**: http://localhost:8080/actuator/health

## 🐳 Option 2: Using Docker

### 1. Clone and Build
```bash
git clone <repository-url>
cd Vaadin
./build.sh
```

### 2. Start with Docker Compose
```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

### 3. Access the Application
- **Application**: http://localhost:8080
- **Grafana**: http://localhost:3000 (admin/admin123)
- **Prometheus**: http://localhost:9090

## ⚡ Option 3: Quick Development Build

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Vaadin
```

### 2. Quick Build
```bash
# Fast build without tests
./quick-build.sh
```

### 3. Run the Application
```bash
# Run with Maven (recommended for development)
mvn spring-boot:run
```

## 🔧 Manual Setup

### 1. Set Java 21
```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Verify Java version
java -version
```

### 2. Build the Application
```bash
# Clean and package
mvn clean package

# Or run directly
mvn spring-boot:run
```

### 3. Start Database (if not using Docker)
```bash
# Start PostgreSQL
docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_DB=musicschool \
  -e POSTGRES_USER=musicschool \
  -e POSTGRES_PASSWORD=musicschool123 \
  postgres:15-alpine
```

## 🎨 First Steps

### 1. Access the Application
Open your browser and go to: http://localhost:8080

### 2. Login
- **Username**: admin
- **Password**: admin123

### 3. Explore the Features
- **Dashboard**: View analytics and statistics
- **Students**: Manage student profiles
- **Courses**: Manage course catalog
- **Enrollments**: Track student enrollments

## 🛠️ Development Workflow

### Daily Development
```bash
# Quick build and run
./quick-build.sh
mvn spring-boot:run
```

### Before Committing
```bash
# Full build with tests
./build.sh
```

### Testing Changes
```bash
# Run tests only
mvn test

# Run specific test
mvn test -Dtest=StudentServiceTest
```

## 🔍 Troubleshooting

### Common Issues

#### 1. Java Version Issues
```bash
# Error: Java version is not 21
# Solution: Install Java 21
sudo apt install openjdk-21-jdk
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
```

#### 2. Maven Issues
```bash
# Error: Maven not found
# Solution: Install Maven
sudo apt install maven
```

#### 3. Permission Issues
```bash
# Error: Permission denied
# Solution: Make scripts executable
chmod +x build.sh quick-build.sh
```

#### 4. Port Already in Use
```bash
# Error: Port 8080 already in use
# Solution: Kill existing process
sudo lsof -ti:8080 | xargs kill -9
```

#### 5. Database Connection Issues
```bash
# Error: Database connection failed
# Solution: Check database status
docker ps | grep postgres
docker logs postgres
```

### Debug Mode

#### Enable Debug Logging
```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

#### Check Application Logs
```bash
# View application logs
tail -f logs/music-school.log

# Or with Docker
docker-compose logs -f musicschool-app
```

## 📊 Monitoring

### Health Checks
```bash
# Application health
curl http://localhost:8080/actuator/health

# Database health
curl http://localhost:8080/actuator/health/db
```

### Metrics
```bash
# Prometheus metrics
curl http://localhost:8080/actuator/prometheus

# Application metrics
curl http://localhost:8080/actuator/metrics
```

## 🚀 Next Steps

### 1. Explore the Code
- **Entities**: `src/main/java/com/musicschool/entity/`
- **Services**: `src/main/java/com/musicschool/service/`
- **UI**: `src/main/java/com/musicschool/ui/`
- **Configuration**: `src/main/resources/`

### 2. Customize the Application
- **Database**: Update `application.yml`
- **Security**: Modify `SecurityConfig.java`
- **UI**: Customize Vaadin components
- **Business Logic**: Update service classes

### 3. Add New Features
- **New Entities**: Create JPA entities
- **New Services**: Add business logic
- **New UI**: Create Vaadin views
- **New APIs**: Add REST controllers

### 4. Deploy to Production
- **Docker**: Use `docker-compose up -d`
- **Cloud**: Follow `DEPLOYMENT.md`
- **Monitoring**: Set up Prometheus and Grafana

## 📚 Additional Resources

### Documentation
- **README.md**: Main project documentation
- **ARCHITECTURE.md**: Architecture overview
- **DEPLOYMENT.md**: Deployment guide
- **BUILD_SCRIPTS.md**: Build scripts documentation
- **API.md**: API documentation

### Support
- **Issues**: Create GitHub issues
- **Documentation**: Check the docs folder
- **Logs**: Review application logs
- **Community**: Join the discussion

## 🎯 Success!

You should now have:
- ✅ **Running Application**: http://localhost:8080
- ✅ **Working Build Scripts**: `./build.sh` and `./quick-build.sh`
- ✅ **Docker Support**: `docker-compose up -d`
- ✅ **Monitoring**: Prometheus and Grafana (if using Docker)
- ✅ **Development Environment**: Ready for coding

## 🆘 Need Help?

1. **Check the Logs**: Look for error messages
2. **Review Documentation**: Read the comprehensive docs
3. **Verify Prerequisites**: Ensure Java 21, Maven, and Docker are installed
4. **Test Build Scripts**: Run `./build.sh` to verify everything works
5. **Create an Issue**: If you're still stuck, create a GitHub issue

---

**🎵 Happy coding with the Music School Management System!**
