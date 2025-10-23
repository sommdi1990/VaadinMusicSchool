# Email Configuration Guide

This document explains how to configure email functionality for different environments.

## Development Environment (Docker)

### Using MailHog (Mock Email Server)

The development environment uses MailHog as a mock email server that captures all emails for testing.

**Features:**

- Captures all outgoing emails
- Web UI to view emails at `http://localhost:8025`
- No real emails sent
- Perfect for development and testing

**Configuration:**

```yaml
# In docker-compose.yml
MAIL_HOST=mailhog
MAIL_PORT=1025
MAIL_USERNAME=
MAIL_PASSWORD=
```

**Usage:**

1. Start the development stack: `docker-compose up -d`
2. Send test emails via API: `POST /api/email/test?email=test@example.com`
3. View captured emails at: `http://localhost:8025`

## Production Environment

### Using Real SMTP Server

For production, configure real SMTP credentials.

**Gmail Configuration:**

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_SSL_TRUST=smtp.gmail.com
```

**Other SMTP Providers:**

```bash
# Outlook/Hotmail
MAIL_HOST=smtp-mail.outlook.com
MAIL_PORT=587

# Yahoo
MAIL_HOST=smtp.mail.yahoo.com
MAIL_PORT=587

# Custom SMTP
MAIL_HOST=your-smtp-server.com
MAIL_PORT=587
MAIL_USERNAME=your-username
MAIL_PASSWORD=your-password
```

## Environment Setup

### Development

```bash
# Use development configuration
docker-compose up -d
```

### Production

```bash
# Use production configuration
docker-compose -f docker-compose.prod.yml up -d
```

## Email Service API

### Test Email

```bash
curl -X POST "http://localhost:8080/music-school/api/email/test?email=test@example.com"
```

### Welcome Email

```bash
curl -X POST "http://localhost:8080/music-school/api/email/welcome?email=student@example.com&name=John%20Doe"
```

### Health Check

```bash
curl "http://localhost:8080/music-school/api/email/health"
```

## Security Notes

1. **Never commit real SMTP credentials to version control**
2. **Use environment variables for production secrets**
3. **Enable 2FA on email accounts and use app passwords**
4. **Consider using dedicated email service providers for production**

## Troubleshooting

### Development Issues

- Check MailHog is running: `docker-compose ps mailhog`
- View MailHog logs: `docker-compose logs mailhog`
- Access MailHog UI: `http://localhost:8025`

### Production Issues

- Verify SMTP credentials are correct
- Check firewall settings for SMTP ports
- Enable "Less secure app access" or use app passwords
- Check email provider's sending limits

## Email Templates

The system includes these email templates:

- Welcome emails for new students
- Notification emails for updates
- Test emails for functionality verification

Customize templates in `EmailService.java` as needed.
