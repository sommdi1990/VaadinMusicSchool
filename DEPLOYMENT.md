# 🚀 Deployment Guide

## Overview

This guide covers deploying the Music School Management System in various environments, from local development to production.

## Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)
- Maven 3.9+ (for local development)
- Git

## Local Development Deployment

### 1. Quick Start with Build Scripts (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd Vaadin

# Full build with tests and Docker preparation
./build.sh

# Or quick build for development
./quick-build.sh

# Run the application
java -jar target/music-school-management-1.0.0.jar
```

### 2. Docker Compose Deployment

```bash
# Start all services with Docker Compose
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f musicschool-app

# Stop services
docker-compose down
```

### 3. Manual Setup

#### Start Database
```bash
# PostgreSQL for local development
docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_DB=musicschool \
  -e POSTGRES_USER=musicschool \
  -e POSTGRES_PASSWORD=musicschool123 \
  postgres:15-alpine
```

#### Build and Run Application
```bash
# Set Java 21
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Build the application
mvn clean package

# Run the application
java -jar target/music-school-management-1.0.0.jar
```

## Production Deployment

### 1. Docker Production Build

```bash
# Build production image
docker build -t musicschool:latest .

# Run with production configuration
docker run -d \
  --name musicschool-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e DB_USERNAME=your_username \
  -e DB_PASSWORD=your_password \
  -e ADMIN_PASSWORD=secure_password \
  musicschool:latest
```

### 2. Docker Compose Production

```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  musicschool-app:
    image: musicschool:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
      - ADMIN_PASSWORD=${ADMIN_PASSWORD}
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/music-school/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

```bash
# Deploy with production compose
docker-compose -f docker-compose.prod.yml up -d
```

## Cloud Deployment

### AWS Deployment

#### 1. ECS (Elastic Container Service)

```json
{
  "family": "musicschool-task",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::account:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "name": "musicschool-app",
      "image": "your-account.dkr.ecr.region.amazonaws.com/musicschool:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "production"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/musicschool",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
```

#### 2. RDS for Database

```bash
# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier musicschool-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --master-username admin \
  --master-user-password your-password \
  --allocated-storage 20
```

### Google Cloud Platform

#### 1. Cloud Run

```yaml
# cloud-run.yaml
apiVersion: serving.knative.dev/v1
kind: Service
metadata:
  name: musicschool
spec:
  template:
    metadata:
      annotations:
        autoscaling.knative.dev/maxScale: "10"
    spec:
      containers:
      - image: gcr.io/your-project/musicschool:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
```

#### 2. Cloud SQL

```bash
# Create Cloud SQL instance
gcloud sql instances create musicschool-db \
  --database-version=POSTGRES_13 \
  --tier=db-f1-micro \
  --region=us-central1
```

### Azure Deployment

#### 1. Container Instances

```yaml
# azure-container-instance.yaml
apiVersion: 2018-10-01
location: eastus
name: musicschool
properties:
  containers:
  - name: musicschool-app
    properties:
      image: your-registry.azurecr.io/musicschool:latest
      resources:
        requests:
          cpu: 1
          memoryInGb: 2
      ports:
      - port: 8080
        protocol: TCP
  osType: Linux
  restartPolicy: Always
  ipAddress:
    type: Public
    ports:
    - protocol: tcp
      port: 8080
```

## Kubernetes Deployment

### 1. Deployment Manifest

```yaml
# k8s-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: musicschool-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: musicschool
  template:
    metadata:
      labels:
        app: musicschool
    spec:
      containers:
      - name: musicschool-app
        image: musicschool:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: musicschool-secrets
              key: db-username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: musicschool-secrets
              key: db-password
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /music-school/actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /music-school/actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

### 2. Service Manifest

```yaml
# k8s-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: musicschool-service
spec:
  selector:
    app: musicschool
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

### 3. Ingress Manifest

```yaml
# k8s-ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: musicschool-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: musicschool.yourdomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: musicschool-service
            port:
              number: 80
```

## Environment Configuration

### Development Environment

```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:26257/musicschool?sslmode=disable
    username: root
    password: ""
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

logging:
  level:
    com.musicschool: DEBUG
```

### Production Environment

```yaml
# application-prod.yml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

logging:
  level:
    com.musicschool: INFO
  file:
    name: /app/logs/music-school.log
```

## Monitoring and Observability

### Prometheus Configuration

```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'musicschool-app'
    static_configs:
      - targets: ['musicschool-app:8080']
    metrics_path: '/music-school/actuator/prometheus'
```

### Grafana Dashboard

```json
{
  "dashboard": {
    "title": "Music School Monitoring",
    "panels": [
      {
        "title": "Application Health",
        "type": "stat",
        "targets": [
          {
            "expr": "up{job=\"musicschool-app\"}"
          }
        ]
      }
    ]
  }
}
```

## Security Considerations

### 1. Environment Variables

```bash
# Set secure passwords
export ADMIN_PASSWORD="your-secure-password"
export DB_PASSWORD="your-database-password"

# Use secrets management
export DB_USERNAME=$(aws secretsmanager get-secret-value --secret-id musicschool/db-username --query SecretString --output text)
```

### 2. Network Security

```yaml
# Security groups for AWS
SecurityGroupIngress:
  - IpProtocol: tcp
    FromPort: 8080
    ToPort: 8080
    SourceSecurityGroupId: !Ref LoadBalancerSecurityGroup
```

### 3. SSL/TLS Configuration

```yaml
# SSL configuration
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-store-type: PKCS12
```

## Backup and Recovery

### Database Backup

```bash
# CockroachDB backup
cockroach sql --host=localhost:26257 --insecure --execute="BACKUP DATABASE musicschool TO 's3://your-bucket/backups/musicschool'"
```

### Application Backup

```bash
# Backup application data
docker exec musicschool-app tar -czf /tmp/app-backup.tar.gz /app/data
docker cp musicschool-app:/tmp/app-backup.tar.gz ./backup-$(date +%Y%m%d).tar.gz
```

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   ```bash
   # Check database connectivity
   docker exec musicschool-app curl -f http://localhost:8080/music-school/actuator/health
   ```

2. **Memory Issues**
   ```bash
   # Check memory usage
   docker stats musicschool-app
   ```

3. **Log Analysis**
   ```bash
   # View application logs
   docker logs -f musicschool-app
   ```

### Health Checks

```bash
# Application health
curl http://localhost:8080/music-school/actuator/health

# Database health
curl http://localhost:8081/health

# Prometheus metrics
curl http://localhost:8080/music-school/actuator/prometheus
```

## Performance Tuning

### JVM Tuning

```bash
# Production JVM options
export JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### Database Tuning

```sql
-- CockroachDB performance settings
SET CLUSTER SETTING sql.defaults.optimizer_use_histograms = true;
SET CLUSTER SETTING sql.defaults.optimizer_use_multicol_stats = true;
```

## Scaling Strategies

### Horizontal Scaling

```yaml
# Kubernetes HPA
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: musicschool-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: musicschool-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

### Vertical Scaling

```yaml
# Resource limits
resources:
  requests:
    memory: "1Gi"
    cpu: "500m"
  limits:
    memory: "2Gi"
    cpu: "1000m"
```

---

## رفع مشکل تنظیمات امنیتی، دیده نشدن تغییرات و دسترسی Home بدون لاگین (Troubleshooting Security & Rebuild)

### مراحل پیشنهادی — فارسی

۱. **ساخت مجدد ایمیج و پاک کردن cache**

```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

۲. **بررسی کپی شدن فایل‌های جدید داخل ایمیج**

اگر در Dockerfile چیزی مانند این دارید:

```dockerfile
COPY src/ /app/src/
COPY src/main/resources/application.yml /app/src/main/resources/application.yml
```

مطابق ساختار پوشه اطمینان حاصل کنید مسیر درست است و فایل کانفیگ جدید واقعاً وارد ایمیج داکر می‌شود.

۳. **بررسی لاگ برنامه (backend/java)**

```bash
docker-compose logs backend  # یا نام سرویس جاوای خود را جایگزین کنید
```

۴. **بررسی متغیرهای env و رمز دیتابیس**

اگر رمز یا نام کاربر دیتابیس در env.production یا docker-compose.yml ست شده، مقدار صحیح را بررسی کنید.
در فرآیند شروع، اگر خطایی مثل `password authentication failed` یا `The server does not support SSL` دریافت می‌کنید باید
تنظیمات database را اصلاح یا با DBA هماهنگ کنید.

۵. **بررسی پاسخ API از درون کانتینر**

```bash
docker exec -it [container-name] curl -I http://localhost:8080
```

----

### خلاصه (en)

- Always rebuild the Docker image after Java/Security config changes:  
  `docker-compose build --no-cache && docker-compose up -d`
- Verify new YAML/Java config is properly copied into the image (check Dockerfile COPY lines).
- Check backend logs with: `docker-compose logs backend`
- Fix DB credentials in `env.production` or `docker-compose.yml` if you see DB errors.
- Test your endpoints *inside* the running container with `curl` for most accurate error info.

---

## علت نیاز به این مراحل

وقتی فایل‌های تنظیمات از جمله Security یا application.yml را تغییر می‌دهید:

- داکر باید مجدداً ایمیج بسازد، وگرنه تغییرات جدید داخل کانتینر نمی‌رود.
- ممکن است مشکل دسترسی دیتابیس در محیط داکر با لوکال فرق داشته باشد.
- برای اینکه Home بدون نیاز به لاگین نمایش داده شود، حتماً باید سرویس جدید (rebuild + restart) شود تا فایل‌های امنیتی
  جدید اعمال شود.

---

اگر بعد از این مراحل ارور یا رفتار غیرمنتظره مشاهده کردید، لاگ کامل سرویس را بررسی کنید و در اختیار تیم توسعه قرار دهید.

---
