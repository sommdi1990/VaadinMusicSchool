# 📚 API Documentation

## Overview

The Music School Management System provides a comprehensive REST API for managing students, instructors, courses, and enrollments. The API follows RESTful principles and provides both JSON responses.

## Base URL

- **Local Development**: `http://localhost:8080/music-school`
- **Production**: `https://your-domain.com/music-school`

## Authentication

The API uses HTTP Basic Authentication. Include credentials in the `Authorization` header:

```http
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

## Content Types

- **Request**: `application/json`
- **Response**: `application/json`

## Error Responses

All error responses follow this format:

```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/students"
}
```

## API Endpoints

### Health and Monitoring

#### GET /actuator/health
Get application health status.

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    }
  }
}
```

#### GET /actuator/metrics
Get application metrics.

**Response:**
```json
{
  "names": [
    "jvm.memory.used",
    "jvm.memory.max",
    "http.server.requests"
  ]
}
```

#### GET /actuator/prometheus
Get Prometheus-formatted metrics.

**Response:**
```
# HELP jvm_memory_used_bytes Used memory in bytes
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="heap",id="PS Eden Space"} 1.23456789E8
```

### Students API

#### GET /api/students
Get all students with optional filtering.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `search` (optional): Search term for name or email
- `level` (optional): Filter by student level

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phone": "+1234567890",
      "dateOfBirth": "2000-01-15",
      "address": "123 Main St",
      "level": "BEGINNER",
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### GET /api/students/{id}
Get a specific student by ID.

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "dateOfBirth": "2000-01-15",
  "address": "123 Main St",
  "level": "BEGINNER",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

#### POST /api/students
Create a new student.

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phone": "+1234567891",
  "dateOfBirth": "1999-05-20",
  "address": "456 Oak Ave",
  "level": "INTERMEDIATE"
}
```

**Response:** 201 Created with the created student object.

#### PUT /api/students/{id}
Update an existing student.

**Request Body:** Same as POST request.

**Response:** 200 OK with the updated student object.

#### DELETE /api/students/{id}
Delete a student.

**Response:** 204 No Content.

### Instructors API

#### GET /api/instructors
Get all instructors with optional filtering.

**Query Parameters:**
- `page` (optional): Page number
- `size` (optional): Page size
- `search` (optional): Search term
- `status` (optional): Filter by status (ACTIVE, INACTIVE, ON_LEAVE)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Alice",
      "lastName": "Johnson",
      "email": "alice.johnson@example.com",
      "phone": "+1234567892",
      "dateOfBirth": "1985-03-10",
      "specialization": "Piano",
      "qualifications": "Master of Music",
      "experienceYears": 10,
      "hourlyRate": 75.00,
      "status": "ACTIVE",
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### GET /api/instructors/{id}
Get a specific instructor by ID.

#### POST /api/instructors
Create a new instructor.

**Request Body:**
```json
{
  "firstName": "Bob",
  "lastName": "Wilson",
  "email": "bob.wilson@example.com",
  "phone": "+1234567893",
  "dateOfBirth": "1980-08-25",
  "specialization": "Guitar",
  "qualifications": "Bachelor of Music",
  "experienceYears": 15,
  "hourlyRate": 80.00,
  "status": "ACTIVE"
}
```

#### PUT /api/instructors/{id}
Update an existing instructor.

#### DELETE /api/instructors/{id}
Delete an instructor.

### Courses API

#### GET /api/courses
Get all courses with optional filtering.

**Query Parameters:**
- `page` (optional): Page number
- `size` (optional): Page size
- `search` (optional): Search term
- `instrument` (optional): Filter by instrument
- `level` (optional): Filter by course level
- `status` (optional): Filter by status

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Beginner Piano",
      "description": "Introduction to piano playing",
      "instrument": "PIANO",
      "level": "BEGINNER",
      "durationMinutes": 60,
      "maxStudents": 10,
      "price": 150.00,
      "startDate": "2024-02-01",
      "endDate": "2024-05-31",
      "scheduleDay": "Monday",
      "scheduleTime": "18:00",
      "status": "ACTIVE",
      "room": "Room A",
      "instructor": {
        "id": 1,
        "firstName": "Alice",
        "lastName": "Johnson"
      },
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### GET /api/courses/{id}
Get a specific course by ID.

#### POST /api/courses
Create a new course.

**Request Body:**
```json
{
  "name": "Advanced Guitar",
  "description": "Advanced guitar techniques",
  "instrument": "GUITAR",
  "level": "ADVANCED",
  "durationMinutes": 90,
  "maxStudents": 8,
  "price": 200.00,
  "startDate": "2024-02-15",
  "endDate": "2024-06-15",
  "scheduleDay": "Wednesday",
  "scheduleTime": "19:00",
  "status": "ACTIVE",
  "room": "Room B",
  "instructorId": 1
}
```

#### PUT /api/courses/{id}
Update an existing course.

#### DELETE /api/courses/{id}
Delete a course.

### Enrollments API

#### GET /api/enrollments
Get all enrollments with optional filtering.

**Query Parameters:**
- `page` (optional): Page number
- `size` (optional): Page size
- `search` (optional): Search term
- `status` (optional): Filter by status
- `studentId` (optional): Filter by student ID
- `courseId` (optional): Filter by course ID

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "enrollmentDate": "2024-01-15",
      "tuitionPaid": 75.00,
      "totalTuition": 150.00,
      "status": "ACTIVE",
      "notes": "First payment received",
      "student": {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe"
      },
      "course": {
        "id": 1,
        "name": "Beginner Piano"
      },
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### GET /api/enrollments/{id}
Get a specific enrollment by ID.

#### POST /api/enrollments
Create a new enrollment.

**Request Body:**
```json
{
  "studentId": 1,
  "courseId": 1,
  "tuitionPaid": 0.00,
  "totalTuition": 150.00,
  "status": "ACTIVE",
  "notes": "New enrollment"
}
```

#### PUT /api/enrollments/{id}
Update an existing enrollment.

#### DELETE /api/enrollments/{id}
Delete an enrollment.

### Reports API

#### GET /api/reports/students-by-level
Get student statistics by level.

**Response:**
```json
{
  "beginner": 25,
  "intermediate": 18,
  "advanced": 12,
  "professional": 5
}
```

#### GET /api/reports/courses-by-status
Get course statistics by status.

**Response:**
```json
{
  "active": 15,
  "inactive": 3,
  "completed": 8,
  "cancelled": 2
}
```

#### GET /api/reports/revenue
Get revenue statistics.

**Response:**
```json
{
  "totalRevenue": 15750.00,
  "outstandingBalance": 2250.00,
  "monthlyRevenue": 5250.00
}
```

## Data Models

### Student
```json
{
  "id": "number",
  "firstName": "string (required)",
  "lastName": "string (required)",
  "email": "string (email format)",
  "phone": "string",
  "dateOfBirth": "string (ISO date)",
  "address": "string",
  "emergencyContact": "string",
  "emergencyPhone": "string",
  "level": "enum (BEGINNER, INTERMEDIATE, ADVANCED, PROFESSIONAL)",
  "notes": "string",
  "createdAt": "string (ISO datetime)",
  "updatedAt": "string (ISO datetime)"
}
```

### Instructor
```json
{
  "id": "number",
  "firstName": "string (required)",
  "lastName": "string (required)",
  "email": "string (email format)",
  "phone": "string",
  "dateOfBirth": "string (ISO date)",
  "address": "string",
  "specialization": "string",
  "qualifications": "string",
  "experienceYears": "number",
  "hourlyRate": "number (decimal)",
  "hireDate": "string (ISO date)",
  "status": "enum (ACTIVE, INACTIVE, ON_LEAVE)",
  "notes": "string",
  "createdAt": "string (ISO datetime)",
  "updatedAt": "string (ISO datetime)"
}
```

### Course
```json
{
  "id": "number",
  "name": "string (required)",
  "description": "string",
  "instrument": "enum (PIANO, GUITAR, VIOLIN, DRUMS, VOICE, SAXOPHONE, TRUMPET, FLUTE, CLARINET, CELLO)",
  "level": "enum (BEGINNER, INTERMEDIATE, ADVANCED, PROFESSIONAL)",
  "durationMinutes": "number",
  "maxStudents": "number",
  "price": "number (decimal)",
  "startDate": "string (ISO date)",
  "endDate": "string (ISO date)",
  "scheduleDay": "string",
  "scheduleTime": "string (HH:mm format)",
  "status": "enum (ACTIVE, INACTIVE, COMPLETED, CANCELLED)",
  "room": "string",
  "instructor": "Instructor object",
  "createdAt": "string (ISO datetime)",
  "updatedAt": "string (ISO datetime)"
}
```

### Enrollment
```json
{
  "id": "number",
  "enrollmentDate": "string (ISO date)",
  "tuitionPaid": "number (decimal)",
  "totalTuition": "number (decimal)",
  "status": "enum (ACTIVE, COMPLETED, DROPPED, SUSPENDED)",
  "notes": "string",
  "student": "Student object",
  "course": "Course object",
  "createdAt": "string (ISO datetime)",
  "updatedAt": "string (ISO datetime)"
}
```

## Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **204 No Content**: Request successful, no content returned
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource conflict (e.g., duplicate email)
- **422 Unprocessable Entity**: Validation errors
- **500 Internal Server Error**: Server error

## Rate Limiting

The API implements rate limiting to prevent abuse:

- **Rate Limit**: 1000 requests per hour per IP
- **Headers**: 
  - `X-RateLimit-Limit`: Request limit
  - `X-RateLimit-Remaining`: Remaining requests
  - `X-RateLimit-Reset`: Reset time

## CORS Configuration

CORS is configured to allow requests from:
- `http://localhost:3000` (development)
- `https://your-domain.com` (production)

## WebSocket Support

Real-time updates are available via WebSocket:

```javascript
const socket = new WebSocket('ws://localhost:8080/music-school/ws');
socket.onmessage = function(event) {
  const data = JSON.parse(event.data);
  // Handle real-time updates
};
```

## SDK Examples

### JavaScript/Node.js

```javascript
const axios = require('axios');

const api = axios.create({
  baseURL: 'http://localhost:8080/music-school/api',
  auth: {
    username: 'admin',
    password: 'admin123'
  }
});

// Get all students
const students = await api.get('/students');

// Create a new student
const newStudent = await api.post('/students', {
  firstName: 'Jane',
  lastName: 'Smith',
  email: 'jane.smith@example.com',
  level: 'BEGINNER'
});
```

### Python

```python
import requests
from requests.auth import HTTPBasicAuth

base_url = 'http://localhost:8080/music-school/api'
auth = HTTPBasicAuth('admin', 'admin123')

# Get all students
response = requests.get(f'{base_url}/students', auth=auth)
students = response.json()

# Create a new student
new_student = {
    'firstName': 'Jane',
    'lastName': 'Smith',
    'email': 'jane.smith@example.com',
    'level': 'BEGINNER'
}
response = requests.post(f'{base_url}/students', json=new_student, auth=auth)
```

### Java

```java
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

RestTemplate restTemplate = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.setBasicAuth("admin", "admin123");

// Get all students
HttpEntity<String> entity = new HttpEntity<>(headers);
ResponseEntity<Student[]> response = restTemplate.exchange(
    "http://localhost:8080/music-school/api/students",
    HttpMethod.GET,
    entity,
    Student[].class
);
Student[] students = response.getBody();
```
