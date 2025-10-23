# 🔐 Default Credentials & Access Information

## Admin Credentials

### Primary Admin User

- **Username**: `admin`
- **Email**: `admin@musicschool.com`
- **Password**: `admin123`
- **Role**: ADMIN
- **Access**: Full system access

## Sample Users

### Instructor Account

- **Username**: `instructor1`
- **Email**: `instructor1@musicschool.com`
- **Password**: `admin123`
- **Role**: INSTRUCTOR
- **Access**: Course management, student view, scheduling

### Student Account

- **Username**: `student1`
- **Email**: `student1@musicschool.com`
- **Password**: `admin123`
- **Role**: STUDENT
- **Access**: Personal dashboard, course enrollment, schedule view

## Sample Data

The system comes pre-loaded with the following sample data:

### Instructors (6)

1. **John Smith** - Piano & Keyboard Specialist
2. **Sarah Johnson** - Guitar & Bass Expert
3. **Mike Davis** - Drums & Percussion Professional
4. **Lisa Wilson** - Violin & Strings Instructor
5. **David Brown** - Vocals & Singing Coach
6. **Emma Taylor** - Saxophone & Woodwinds Specialist

### Students (5)

1. **Alice Johnson** - Beginner Piano Student
2. **Bob Smith** - Intermediate Guitar Student
3. **Carol Davis** - Beginner Violin Student
4. **Daniel Wilson** - Advanced Drum Student
5. **Eva Brown** - Intermediate Vocal Student

### Courses (6)

1. **Piano for Beginners** - Monday 4:00 PM
2. **Guitar Fundamentals** - Tuesday 5:00 PM
3. **Drum Workshop** - Wednesday 6:00 PM
4. **Violin Basics** - Thursday 4:30 PM
5. **Voice Training** - Friday 5:30 PM
6. **Saxophone Jazz** - Saturday 10:00 AM

## Access URLs

### Application URLs

- **Main Application**: http://localhost:8080/music-school/
- **Home Page**: http://localhost:8080/music-school/
- **Dashboard**: http://localhost:8080/music-school/dashboard
- **Students**: http://localhost:8080/music-school/students

### Admin & Monitoring URLs

- **Database Admin**: http://localhost:8081
- **Grafana Dashboard**: http://localhost:3000
    - Username: `admin`
    - Password: `admin`
- **Prometheus Metrics**: http://localhost:9090
- **Mock Email Server**: http://localhost:3001

## Security Notes

### Password Security

- All passwords are hashed using BCrypt
- Default passwords should be changed in production
- Account lockout after 5 failed login attempts
- 1-hour lockout duration for failed attempts

### User Roles

- **SUPER_ADMIN**: Full system access across all tenants
- **ADMIN**: Full access within tenant
- **INSTRUCTOR**: Course and student management
- **STUDENT**: Personal dashboard and enrollment
- **PARENT**: Student progress monitoring
- **RECEPTIONIST**: Front desk operations
- **ACCOUNTANT**: Financial reporting and payments

## Database Information

### Connection Details

- **Host**: localhost
- **Port**: 26257
- **Database**: musicschool
- **Username**: root
- **Password**: (no password for development)
- **SSL Mode**: disable

### Admin Interface

- **CockroachDB Admin UI**: http://localhost:8081
- **SQL Access**: Use CockroachDB client or admin UI

## Email Configuration

### Mock Email Server

- **SMTP Port**: 1025
- **HTTP Interface**: http://localhost:3001
- **API Endpoint**: http://localhost:3001/api/emails

### Email Testing

- All emails are captured by the mock server
- No actual emails are sent in development
- Check mock server interface for sent emails

## Troubleshooting

### Common Issues

1. **Port Conflicts**: Ensure ports 8080, 8081, 3000, 9090, 1025, 3001 are available
2. **Database Connection**: Check if CockroachDB is running on port 26257
3. **Memory Issues**: Ensure Docker has sufficient memory allocated
4. **Login Issues**: Verify credentials and check account lockout status

### Reset Passwords

To reset a password, connect to the database and update the password hash:

```sql
UPDATE users SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi' 
WHERE username = 'admin';
```

### Account Unlock

To unlock a locked account:

```sql
UPDATE users SET locked_until = NULL, login_attempts = 0 
WHERE username = 'admin';
```
