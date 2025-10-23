# Music School Mock Email Server

A dedicated mock email server for development and testing phases of the Music School Management System.

## Features

- **SMTP Server**: Accepts emails on port 1025
- **Web Interface**: View captured emails at port 3000
- **REST API**: Programmatic access to emails
- **Real-time Updates**: Auto-refreshing web interface
- **Email Management**: View, delete, and clear emails
- **Statistics**: Track email metrics

## Quick Start

### Development Mode

```bash
cd mock-email-server
npm install
npm start
```

### Docker Mode

```bash
docker build -t music-school-mock-email .
docker run -p 3000:3000 -p 1025:1025 music-school-mock-email
```

## API Endpoints

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

## Web Interface

Access the web interface at `http://localhost:3000` to:

- View all captured emails
- See email statistics
- Clear emails
- Real-time updates

## Configuration

Environment variables:

- `PORT`: HTTP server port (default: 3000)
- `SMTP_PORT`: SMTP server port (default: 1025)

## Integration

This mock server is automatically used in development and testing environments. The main application will connect to it
instead of a real SMTP server.

## Email Format

Captured emails include:

- Unique ID
- From/To addresses
- Subject and content (text/HTML)
- Timestamp
- Headers
- Attachments (if any)

## Development

```bash
# Install dependencies
npm install

# Start in development mode
npm run dev

# Run tests
npm test
```
