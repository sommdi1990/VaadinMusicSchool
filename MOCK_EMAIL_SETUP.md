# Mock Email Server Setup

This document explains the custom mock email server module for the Music School Management System.

## 🎯 Overview

The mock email server is a dedicated Node.js application that provides:

- **SMTP Server**: Accepts emails on port 1025
- **Web Interface**: View captured emails at port 3000
- **REST API**: Programmatic access to emails
- **Real-time Updates**: Auto-refreshing interface
- **Email Management**: View, delete, and clear emails

## 📁 Module Structure

```
mock-email-server/
├── Dockerfile              # Docker configuration
├── package.json            # Node.js dependencies
├── server.js              # Main server application
└── README.md              # Module documentation
```

## 🚀 Quick Start

### Development Mode

```bash
cd mock-email-server
npm install
npm start
```

### Docker Mode

```bash
# Build and run the mock server
docker-compose up -d mock-email-server

# Or build manually
docker build -t music-school-mock-email ./mock-email-server
docker run -p 3000:3000 -p 1025:1025 music-school-mock-email
```

## 🔧 Integration

### Development Environment

The mock server is automatically used in development:

```bash
# Start development stack
docker-compose up -d

# The app will connect to mock-email-server instead of real SMTP
```

### Testing Environment

For isolated testing:

```bash
# Start test environment
docker-compose -f docker-compose.test.yml up -d

# Run email tests
./scripts/test-email.sh
```

## 📧 API Endpoints

### Health Check

```bash
GET /health
```

### Get All Emails

```bash
GET /api/emails?limit=50&offset=0
```

### Get Specific Email

```bash
GET /api/emails/:id
```

### Delete Email

```bash
DELETE /api/emails/:id
```

### Clear All Emails

```bash
DELETE /api/emails
```

### Get Statistics

```bash
GET /api/stats
```

## 🌐 Web Interface

Access the web interface at `http://localhost:3000` to:

- View all captured emails
- See email statistics
- Clear emails
- Real-time updates

## 🧪 Testing

### Automated Testing

```bash
# Run the comprehensive email test
./scripts/test-email.sh
```

### Manual Testing

```bash
# Send test email via API
curl -X POST "http://localhost:8080/music-school/api/email/test?email=test@example.com"

# Send welcome email
curl -X POST "http://localhost:8080/music-school/api/email/welcome?email=student@example.com&name=John%20Doe"

# Check captured emails
curl "http://localhost:3000/api/emails"
```

## 🔄 Environment Configurations

### Development (docker-compose.yml)

- Uses `mock-email-server` service
- SMTP: `mock-email-server:1025`
- Web UI: `http://localhost:3000`

### Testing (docker-compose.test.yml)

- Isolated test environment
- H2 in-memory database
- Mock email server for email testing

### Production (docker-compose.prod.yml)

- Uses real SMTP servers
- No mock server
- Production email configuration

## 📊 Email Format

Captured emails include:

```json
{
  "id": "unique-uuid",
  "from": "sender@example.com",
  "to": "recipient@example.com",
  "subject": "Email Subject",
  "text": "Plain text content",
  "html": "<p>HTML content</p>",
  "date": "2025-10-23T07:30:00.000Z",
  "headers": {...},
  "attachments": [...]
}
```

## 🛠️ Development

### Local Development

```bash
cd mock-email-server
npm install
npm run dev  # With nodemon for auto-restart
```

### Adding Features

1. Modify `server.js` for new functionality
2. Update `package.json` for new dependencies
3. Test with `./scripts/test-email.sh`
4. Update documentation

## 🔍 Troubleshooting

### Mock Server Not Starting

```bash
# Check logs
docker-compose logs mock-email-server

# Check health
curl http://localhost:3000/health
```

### Emails Not Captured

```bash
# Check SMTP connection
telnet localhost 1025

# Check app configuration
docker-compose logs musicschool-app
```

### Web Interface Not Loading

```bash
# Check if port is accessible
curl http://localhost:3000

# Check Docker port mapping
docker-compose ps mock-email-server
```

## 📈 Monitoring

### Statistics

- Total emails received
- Last email timestamp
- Email count by sender/recipient
- Error rates

### Health Checks

- SMTP server status
- Web interface availability
- Email processing status

## 🔒 Security

- No authentication required (development only)
- Emails stored in memory (not persistent)
- CORS enabled for web interface
- No sensitive data logging

## 🚀 Production Notes

- Mock server is **NOT** used in production
- Production uses real SMTP servers
- Mock server is only for development/testing
- No persistent storage (emails lost on restart)

## 📝 Configuration Files

- `docker-compose.yml` - Development with mock server
- `docker-compose.test.yml` - Testing environment
- `docker-compose.prod.yml` - Production with real SMTP
- `application-docker.yml` - App config for development
- `application-test.yml` - App config for testing
- `application-production.yml` - App config for production
