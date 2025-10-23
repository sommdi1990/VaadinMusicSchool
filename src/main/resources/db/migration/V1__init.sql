-- Create tenant table
CREATE TABLE IF NOT EXISTS tenant (
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    description VARCHAR(1000),
    domain VARCHAR(128) UNIQUE NOT NULL,
    contact_email VARCHAR(128),
    contact_phone VARCHAR(50),
    address VARCHAR(500),
    logo_url VARCHAR(255),
    website VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    subscription_plan VARCHAR(50),
    max_students INTEGER,
    max_instructors INTEGER,
    max_courses INTEGER,
    subscription_start_date TIMESTAMP,
    subscription_end_date TIMESTAMP,
    settings TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    last_login TIMESTAMP,
    login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP,
    password_reset_token VARCHAR(255),
    password_reset_expires TIMESTAMP,
    email_verified BOOLEAN DEFAULT FALSE,
    email_verification_token VARCHAR(255),
    tenant_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);

-- Create user_roles table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(128) UNIQUE,
    phone VARCHAR(20),
    date_of_birth DATE NOT NULL,
    address VARCHAR(255),
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    level VARCHAR(20) DEFAULT 'BEGINNER',
    notes TEXT,
    tenant_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);

-- Create instructors table
CREATE TABLE IF NOT EXISTS instructors (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(128) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    address VARCHAR(255),
    hire_date DATE NOT NULL,
    salary DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    specialization VARCHAR(255),
    bio TEXT,
    tenant_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);

-- Create courses table
CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    instructor_id BIGINT NOT NULL,
    instrument VARCHAR(20),
    level VARCHAR(20),
    duration_minutes INTEGER DEFAULT 60,
    max_students INTEGER DEFAULT 10,
    price DECIMAL(10,2),
    start_date DATE,
    end_date DATE,
    schedule_day VARCHAR(20),
    schedule_time TIME,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    room VARCHAR(50),
    tenant_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (instructor_id) REFERENCES instructors(id),
    FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);

-- Create enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Create schedules table
CREATE TABLE IF NOT EXISTS schedules (
    id SERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    title VARCHAR(100),
    description TEXT,
    type VARCHAR(20),
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    room VARCHAR(50),
    recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern TEXT,
    parent_schedule_id BIGINT,
    instructor_id BIGINT,
    student_id BIGINT,
    course_id BIGINT,
    notes TEXT,
    cancelled_at TIMESTAMP,
    cancellation_reason VARCHAR(255),
    rescheduled_from TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (instructor_id) REFERENCES instructors(id),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (parent_schedule_id) REFERENCES schedules(id)
);

-- Create payments table
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(20),
    status VARCHAR(20) DEFAULT 'PENDING',
    transaction_id VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Create audit_logs table
CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    old_values TEXT,
    new_values TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create reports table
CREATE TABLE IF NOT EXISTS reports (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL,
    parameters TEXT,
    generated_at TIMESTAMP,
    file_path VARCHAR(500),
    file_size BIGINT,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create schedule_conflicts table
CREATE TABLE IF NOT EXISTS schedule_conflicts (
    id SERIAL PRIMARY KEY,
    schedule_id BIGINT NOT NULL,
    conflict_type VARCHAR(20) NOT NULL,
    conflict_description TEXT,
    resolved BOOLEAN DEFAULT FALSE,
    resolved_at TIMESTAMP,
    resolved_by BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (schedule_id) REFERENCES schedules(id),
    FOREIGN KEY (resolved_by) REFERENCES users(id)
);

-- Insert default tenant
INSERT INTO tenant (name, domain, contact_email, status) 
VALUES ('Default Music School', 'default.musicschool.com', 'admin@musicschool.com', 'ACTIVE')
ON CONFLICT (domain) DO NOTHING;

-- Insert default admin user
INSERT INTO users (username, email, password, first_name, last_name, tenant_id, email_verified)
SELECT 'admin', 'admin@musicschool.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', t.id, true
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (username) DO NOTHING;

-- Insert admin role
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'ADMIN'
FROM users u WHERE u.username = 'admin'
ON CONFLICT (user_id, role) DO NOTHING;