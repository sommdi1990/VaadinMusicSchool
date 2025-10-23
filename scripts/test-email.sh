#!/bin/bash

# Music School Email Testing Script
# Tests the mock email server functionality

set -e

echo "🎵 Music School Email Testing Script"
echo "===================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
MOCK_SERVER_URL="http://localhost:3000"
APP_URL="http://localhost:8080/music-school"
TEST_EMAIL="test@musicschool.com"

echo -e "${BLUE}📧 Testing Mock Email Server...${NC}"

# Check if mock server is running
echo -e "${YELLOW}1. Checking Mock Email Server Health...${NC}"
if curl -s -f "$MOCK_SERVER_URL/health" > /dev/null; then
    echo -e "${GREEN}✅ Mock Email Server is running${NC}"
else
    echo -e "${RED}❌ Mock Email Server is not running${NC}"
    echo "Please start the mock server first:"
    echo "  docker-compose up -d mock-email-server"
    exit 1
fi

# Check if main app is running
echo -e "${YELLOW}2. Checking Main Application...${NC}"
if curl -s -f "$APP_URL/actuator/health" > /dev/null; then
    echo -e "${GREEN}✅ Main Application is running${NC}"
else
    echo -e "${RED}❌ Main Application is not running${NC}"
    echo "Please start the main application first:"
    echo "  docker-compose up -d musicschool-app"
    exit 1
fi

# Clear existing emails
echo -e "${YELLOW}3. Clearing existing emails...${NC}"
curl -s -X DELETE "$MOCK_SERVER_URL/api/emails" > /dev/null
echo -e "${GREEN}✅ Existing emails cleared${NC}"

# Test 1: Send test email
echo -e "${YELLOW}4. Testing Email API - Test Email...${NC}"
RESPONSE=$(curl -s -X POST "$APP_URL/api/email/test?email=$TEST_EMAIL")
if echo "$RESPONSE" | grep -q "success"; then
    echo -e "${GREEN}✅ Test email sent successfully${NC}"
else
    echo -e "${RED}❌ Failed to send test email${NC}"
    echo "Response: $RESPONSE"
fi

# Test 2: Send welcome email
echo -e "${YELLOW}5. Testing Email API - Welcome Email...${NC}"
RESPONSE=$(curl -s -X POST "$APP_URL/api/email/welcome?email=$TEST_EMAIL&name=Test%20Student")
if echo "$RESPONSE" | grep -q "success"; then
    echo -e "${GREEN}✅ Welcome email sent successfully${NC}"
else
    echo -e "${RED}❌ Failed to send welcome email${NC}"
    echo "Response: $RESPONSE"
fi

# Wait a moment for emails to be processed
echo -e "${YELLOW}6. Waiting for emails to be processed...${NC}"
sleep 3

# Check captured emails
echo -e "${YELLOW}7. Checking Captured Emails...${NC}"
EMAILS_RESPONSE=$(curl -s "$MOCK_SERVER_URL/api/emails")
EMAIL_COUNT=$(echo "$EMAILS_RESPONSE" | grep -o '"total":[0-9]*' | grep -o '[0-9]*')

if [ "$EMAIL_COUNT" -gt 0 ]; then
    echo -e "${GREEN}✅ $EMAIL_COUNT emails captured successfully${NC}"
    
    # Show email details
    echo -e "${BLUE}📧 Captured Emails:${NC}"
    echo "$EMAILS_RESPONSE" | jq -r '.emails[] | "  📨 \(.subject) (from: \(.from) to: \(.to))"'
else
    echo -e "${RED}❌ No emails captured${NC}"
fi

# Test 3: Check email statistics
echo -e "${YELLOW}8. Checking Email Statistics...${NC}"
STATS_RESPONSE=$(curl -s "$MOCK_SERVER_URL/api/stats")
echo -e "${BLUE}📊 Email Statistics:${NC}"
echo "$STATS_RESPONSE" | jq -r '.stats | "  Total Received: \(.totalReceived)\n  Last Received: \(.lastReceived)"'

# Test 4: Email health check
echo -e "${YELLOW}9. Testing Email Health Check...${NC}"
HEALTH_RESPONSE=$(curl -s "$APP_URL/api/email/health")
if echo "$HEALTH_RESPONSE" | grep -q "running"; then
    echo -e "${GREEN}✅ Email service health check passed${NC}"
else
    echo -e "${RED}❌ Email service health check failed${NC}"
    echo "Response: $HEALTH_RESPONSE"
fi

echo ""
echo -e "${GREEN}🎉 Email Testing Complete!${NC}"
echo ""
echo -e "${BLUE}📋 Summary:${NC}"
echo "  • Mock Server: $MOCK_SERVER_URL"
echo "  • Web Interface: $MOCK_SERVER_URL"
echo "  • API Endpoint: $MOCK_SERVER_URL/api/emails"
echo "  • Main App: $APP_URL"
echo ""
echo -e "${YELLOW}💡 Tips:${NC}"
echo "  • View emails in browser: $MOCK_SERVER_URL"
echo "  • Check app health: $APP_URL/actuator/health"
echo "  • View logs: docker-compose logs mock-email-server"
