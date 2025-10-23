# Docker Native Setup with GraalVM 21

This document describes the Docker setup for the Music School Management System using GraalVM 21 with native
compilation, CockroachDB, and Vaadin.

## Overview

The project is configured to run with:

- **GraalVM 21** with native compilation for optimal performance
- **CockroachDB** as the database
- **Vaadin** for the frontend
- **Spring Boot** with native image support
- **Docker Compose** for orchestration

## Architecture

### Docker Images Used

1. **Build Stage**: `ghcr.io/graalvm/native-image-community:21-ol9`
    - GraalVM 21 with native-image tool
    - Maven for building
    - Node.js for Vaadin frontend compilation

2. **Runtime Stage**: `ghcr.io/graalvm/native-image-community:21-ol9`
    - Minimal runtime with native executable
    - Only necessary dependencies

3. **Database**: `cockroachdb/cockroach:v23.2.0`
    - Latest stable CockroachDB version
    - Single-node setup for development

## Configuration Files

### Dockerfile

The Dockerfile uses a multi-stage build:

1. **Build Stage**:
    - Uses GraalVM 21 with native-image
    - Installs Maven and Node.js
    - Builds the application with `-Pproduction -Pnative` profiles
    - Creates native executable

2. **Runtime Stage**:
    - Minimal runtime image
    - Copies native executable
    - Copies Vaadin frontend resources
    - Runs as non-root user

### pom.xml Changes

Added native compilation support:

```xml
<properties>
    <native.maven.plugin.version>0.9.28</native.maven.plugin.version>
    <graalvm.version>21</graalvm.version>
</properties>
```

**Native Maven Plugin Configuration**:

- Uses `org.graalvm.buildtools:native-maven-plugin`
- Configured with proper build arguments for Spring Boot and Vaadin
- Includes initialization hints for various frameworks

**Profiles**:

- `production`: Builds frontend in production mode
- `native`: Enables native compilation

### Docker Compose

Updated both `docker-compose.yml` and `docker-compose.prod.yml`:

- **CockroachDB**: Updated to v23.2.0 with proper networking
- **Application**: Configured for native execution
- **Networking**: Proper service discovery
- **Health Checks**: Configured for all services

## Build Process

### Maven Profiles

1. **Development Build**:
   ```bash
   mvn clean package
   ```

2. **Production Build**:
   ```bash
   mvn clean package -Pproduction
   ```

3. **Native Build**:
   ```bash
   mvn clean package -Pproduction -Pnative
   ```

### Docker Build

```bash
# Build the Docker image
docker build -t musicschool-app .

# Or using docker-compose
docker-compose build
```

## Running the Application

### Development Mode

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f musicschool-app

# Stop services
docker-compose down
```

### Production Mode

```bash
# Start production services
docker-compose -f docker-compose.prod.yml up -d

# View logs
docker-compose -f docker-compose.prod.yml logs -f musicschool-app
```

## Services

### Application Service

- **Port**: 8080
- **Health Check**: `/actuator/health`
- **Native Executable**: `/app/music-school-management`

### CockroachDB Service

- **Port**: 26257 (SQL), 8081 (Admin UI)
- **Database**: musicschool
- **Mode**: Single-node, insecure

### Redis Service

- **Port**: 6379
- **Purpose**: Caching and session management

### Monitoring Services

- **Prometheus**: Port 9090
- **Grafana**: Port 3000

## Environment Variables

### Application Variables

- `SPRING_PROFILES_ACTIVE`: docker/production
- `DB_HOST`: cockroachdb
- `DB_PORT`: 26257
- `DB_NAME`: musicschool
- `DB_USERNAME`: root
- `DB_PASSWORD`: (empty for insecure mode)

### CockroachDB Variables

- `COCKROACH_DATABASE`: musicschool
- `COCKROACH_DEVELOPER_MODE`: true

## Native Compilation Benefits

1. **Fast Startup**: Native executables start in milliseconds
2. **Low Memory**: Reduced memory footprint
3. **Better Performance**: Optimized for specific hardware
4. **Smaller Images**: Minimal runtime dependencies

## Troubleshooting

### Common Issues

1. **Native Compilation Fails**:
    - Check GraalVM version compatibility
    - Verify build arguments in pom.xml
    - Ensure all dependencies are compatible

2. **Database Connection Issues**:
    - Verify CockroachDB is running
    - Check network connectivity
    - Verify connection string format

3. **Vaadin Frontend Issues**:
    - Ensure Node.js is properly installed
    - Check frontend build process
    - Verify resource copying in Dockerfile

### Debug Commands

```bash
# Check container logs
docker logs musicschool-app

# Check database connectivity
docker exec -it musicschool-db cockroach sql --insecure

# Check native executable
docker exec -it musicschool-app ls -la /app/

# Check health status
curl http://localhost:8080/actuator/health
```

## Performance Optimization

### Native Image Configuration

The native image is configured with:

- `--no-fallback`: Ensures pure native execution
- `--install-exit-handlers`: Proper shutdown handling
- `--enable-http/https`: Network protocol support
- Initialization hints for various frameworks

### Memory Settings

- **Build**: Uses container memory limits
- **Runtime**: Optimized for native execution
- **Database**: Configured for development workload

## Security Considerations

1. **Non-root User**: Application runs as `musicschool` user
2. **Network Isolation**: Services communicate via Docker network
3. **Database Security**: Insecure mode for development only
4. **Resource Limits**: Configured memory and CPU limits

## Monitoring and Logging

### Health Checks

- Application: `/actuator/health`
- Database: CockroachDB health endpoint
- Redis: `redis-cli ping`

### Metrics

- Prometheus: Collects application metrics
- Grafana: Visualization dashboard
- Spring Boot Actuator: Application metrics

## Development Workflow

1. **Code Changes**: Edit source code
2. **Build**: `mvn clean package -Pproduction -Pnative`
3. **Docker Build**: `docker-compose build`
4. **Test**: `docker-compose up`
5. **Deploy**: `docker-compose -f docker-compose.prod.yml up -d`

## Production Deployment

1. **Environment Setup**: Configure production variables
2. **Database Setup**: Initialize CockroachDB cluster
3. **Application Deployment**: Deploy native executable
4. **Monitoring**: Set up Prometheus and Grafana
5. **Backup**: Configure database backups

## Support and Maintenance

- **Logs**: Check application and database logs
- **Metrics**: Monitor performance via Grafana
- **Updates**: Regular dependency updates
- **Backup**: Database backup procedures

This setup provides a robust, high-performance environment for the Music School Management System with native
compilation benefits.
