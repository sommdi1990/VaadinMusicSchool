-- V3: Add sample data and improvements
-- This migration adds sample data for testing and improves the database structure

-- Add some sample instructors
INSERT INTO instructors (first_name, last_name, email, phone, date_of_birth, address, hire_date, salary, status, specialization, bio, tenant_id)
SELECT 
    'John', 'Smith', 'john.smith@musicschool.com', '+1-555-0101', '1985-03-15', '123 Music St, City', '2020-01-15', 50000.00, 'ACTIVE', 'Piano & Keyboard', 'Experienced piano instructor with 15 years of teaching experience.', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO instructors (first_name, last_name, email, phone, date_of_birth, address, hire_date, salary, status, specialization, bio, tenant_id)
SELECT 
    'Sarah', 'Johnson', 'sarah.johnson@musicschool.com', '+1-555-0102', '1988-07-22', '456 Guitar Ave, City', '2019-06-01', 48000.00, 'ACTIVE', 'Guitar & Bass', 'Professional guitarist and music theory expert.', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO instructors (first_name, last_name, email, phone, date_of_birth, address, hire_date, salary, status, specialization, bio, tenant_id)
SELECT 
    'Mike', 'Davis', 'mike.davis@musicschool.com', '+1-555-0103', '1982-11-08', '789 Drum Lane, City', '2018-09-10', 52000.00, 'ACTIVE', 'Drums & Percussion', 'Drummer with extensive performance and teaching background.', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO instructors (first_name, last_name, email, phone, date_of_birth, address, hire_date, salary, status, specialization, bio, tenant_id)
SELECT 
    'Lisa', 'Wilson', 'lisa.wilson@musicschool.com', '+1-555-0104', '1990-04-12', '321 Violin St, City', '2021-02-01', 46000.00, 'ACTIVE', 'Violin & Strings', 'Classical violinist with conservatory training.', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO instructors (first_name, last_name, email, phone, date_of_birth, address, hire_date, salary, status, specialization, bio, tenant_id)
SELECT 
    'David', 'Brown', 'david.brown@musicschool.com', '+1-555-0105', '1987-09-30', '654 Vocals Blvd, City', '2020-08-15', 49000.00, 'ACTIVE', 'Vocals & Singing', 'Professional vocalist and voice coach.', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO instructors (first_name, last_name, email, phone, date_of_birth, address, hire_date, salary, status, specialization, bio, tenant_id)
SELECT 
    'Emma', 'Taylor', 'emma.taylor@musicschool.com', '+1-555-0106', '1992-12-05', '987 Saxophone Dr, City', '2022-01-10', 44000.00, 'ACTIVE', 'Saxophone & Woodwinds', 'Jazz saxophonist with performance experience.', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

-- Add some sample students
INSERT INTO students (first_name, last_name, email, phone, date_of_birth, address, emergency_contact, emergency_phone, level, notes, tenant_id)
SELECT 
    'Alice', 'Johnson', 'alice.johnson@email.com', '+1-555-1001', '2010-05-15', '100 Student St, City', 'Parent Johnson', '+1-555-2001', 'BEGINNER', 'New student interested in piano', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO students (first_name, last_name, email, phone, date_of_birth, address, emergency_contact, emergency_phone, level, notes, tenant_id)
SELECT 
    'Bob', 'Smith', 'bob.smith@email.com', '+1-555-1002', '2008-08-22', '200 Student Ave, City', 'Parent Smith', '+1-555-2002', 'INTERMEDIATE', 'Guitar student with 2 years experience', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO students (first_name, last_name, email, phone, date_of_birth, address, emergency_contact, emergency_phone, level, notes, tenant_id)
SELECT 
    'Carol', 'Davis', 'carol.davis@email.com', '+1-555-1003', '2012-03-10', '300 Student Lane, City', 'Parent Davis', '+1-555-2003', 'BEGINNER', 'Young student starting with violin', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO students (first_name, last_name, email, phone, date_of_birth, address, emergency_contact, emergency_phone, level, notes, tenant_id)
SELECT 
    'Daniel', 'Wilson', 'daniel.wilson@email.com', '+1-555-1004', '2009-11-18', '400 Student Blvd, City', 'Parent Wilson', '+1-555-2004', 'ADVANCED', 'Experienced drummer', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

INSERT INTO students (first_name, last_name, email, phone, date_of_birth, address, emergency_contact, emergency_phone, level, notes, tenant_id)
SELECT 
    'Eva', 'Brown', 'eva.brown@email.com', '+1-555-1005', '2011-07-25', '500 Student Dr, City', 'Parent Brown', '+1-555-2005', 'INTERMEDIATE', 'Vocal student with good pitch', t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (email) DO NOTHING;

-- Add some sample courses
INSERT INTO courses (name, description, instructor_id, instrument, level, duration_minutes, max_students, price, start_date, end_date, schedule_day, schedule_time, status, room, tenant_id)
SELECT 
    'Piano for Beginners', 'Learn the basics of piano playing', i.id, 'PIANO', 'BEGINNER', 60, 8, 120.00, '2024-01-15', '2024-06-15', 'MONDAY', '16:00:00', 'ACTIVE', 'Room A', t.id
FROM instructors i, tenant t 
WHERE i.email = 'john.smith@musicschool.com' AND t.domain = 'default.musicschool.com'
ON CONFLICT DO NOTHING;

INSERT INTO courses (name, description, instructor_id, instrument, level, duration_minutes, max_students, price, start_date, end_date, schedule_day, schedule_time, status, room, tenant_id)
SELECT 
    'Guitar Fundamentals', 'Master the basics of guitar playing', i.id, 'GUITAR', 'BEGINNER', 60, 10, 100.00, '2024-01-16', '2024-06-16', 'TUESDAY', '17:00:00', 'ACTIVE', 'Room B', t.id
FROM instructors i, tenant t 
WHERE i.email = 'sarah.johnson@musicschool.com' AND t.domain = 'default.musicschool.com'
ON CONFLICT DO NOTHING;

INSERT INTO courses (name, description, instructor_id, instrument, level, duration_minutes, max_students, price, start_date, end_date, schedule_day, schedule_time, status, room, tenant_id)
SELECT 
    'Drum Workshop', 'Advanced drumming techniques', i.id, 'DRUMS', 'ADVANCED', 90, 6, 150.00, '2024-01-17', '2024-06-17', 'WEDNESDAY', '18:00:00', 'ACTIVE', 'Room C', t.id
FROM instructors i, tenant t 
WHERE i.email = 'mike.davis@musicschool.com' AND t.domain = 'default.musicschool.com'
ON CONFLICT DO NOTHING;

INSERT INTO courses (name, description, instructor_id, instrument, level, duration_minutes, max_students, price, start_date, end_date, schedule_day, schedule_time, status, room, tenant_id)
SELECT 
    'Violin Basics', 'Introduction to violin playing', i.id, 'VIOLIN', 'BEGINNER', 45, 8, 110.00, '2024-01-18', '2024-06-18', 'THURSDAY', '16:30:00', 'ACTIVE', 'Room D', t.id
FROM instructors i, tenant t 
WHERE i.email = 'lisa.wilson@musicschool.com' AND t.domain = 'default.musicschool.com'
ON CONFLICT DO NOTHING;

INSERT INTO courses (name, description, instructor_id, instrument, level, duration_minutes, max_students, price, start_date, end_date, schedule_day, schedule_time, status, room, tenant_id)
SELECT 
    'Voice Training', 'Develop your singing voice', i.id, 'VOCALS', 'INTERMEDIATE', 60, 12, 130.00, '2024-01-19', '2024-06-19', 'FRIDAY', '17:30:00', 'ACTIVE', 'Room E', t.id
FROM instructors i, tenant t 
WHERE i.email = 'david.brown@musicschool.com' AND t.domain = 'default.musicschool.com'
ON CONFLICT DO NOTHING;

INSERT INTO courses (name, description, instructor_id, instrument, level, duration_minutes, max_students, price, start_date, end_date, schedule_day, schedule_time, status, room, tenant_id)
SELECT 
    'Saxophone Jazz', 'Jazz saxophone techniques', i.id, 'SAXOPHONE', 'INTERMEDIATE', 75, 8, 140.00, '2024-01-20', '2024-06-20', 'SATURDAY', '10:00:00', 'ACTIVE', 'Room F', t.id
FROM instructors i, tenant t 
WHERE i.email = 'emma.taylor@musicschool.com' AND t.domain = 'default.musicschool.com'
ON CONFLICT DO NOTHING;

-- Add some sample enrollments
INSERT INTO enrollments (student_id, course_id, enrollment_date, status, notes)
SELECT s.id, c.id, '2024-01-10', 'ACTIVE', 'Enrolled in piano course'
FROM students s, courses c, tenant t
WHERE s.email = 'alice.johnson@email.com' 
  AND c.name = 'Piano for Beginners' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;

INSERT INTO enrollments (student_id, course_id, enrollment_date, status, notes)
SELECT s.id, c.id, '2024-01-11', 'ACTIVE', 'Enrolled in guitar course'
FROM students s, courses c, tenant t
WHERE s.email = 'bob.smith@email.com' 
  AND c.name = 'Guitar Fundamentals' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;

INSERT INTO enrollments (student_id, course_id, enrollment_date, status, notes)
SELECT s.id, c.id, '2024-01-12', 'ACTIVE', 'Enrolled in violin course'
FROM students s, courses c, tenant t
WHERE s.email = 'carol.davis@email.com' 
  AND c.name = 'Violin Basics' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;

INSERT INTO enrollments (student_id, course_id, enrollment_date, status, notes)
SELECT s.id, c.id, '2024-01-13', 'ACTIVE', 'Enrolled in drum workshop'
FROM students s, courses c, tenant t
WHERE s.email = 'daniel.wilson@email.com' 
  AND c.name = 'Drum Workshop' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;

INSERT INTO enrollments (student_id, course_id, enrollment_date, status, notes)
SELECT s.id, c.id, '2024-01-14', 'ACTIVE', 'Enrolled in voice training'
FROM students s, courses c, tenant t
WHERE s.email = 'eva.brown@email.com' 
  AND c.name = 'Voice Training' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;

-- Add some sample users for testing
INSERT INTO users (username, email, password, first_name, last_name, phone, status, email_verified, tenant_id)
SELECT 
    'instructor1', 'instructor1@musicschool.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Smith', '+1-555-0101', 'ACTIVE', true, t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, email, password, first_name, last_name, phone, status, email_verified, tenant_id)
SELECT 
    'student1', 'student1@musicschool.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Alice', 'Johnson', '+1-555-1001', 'ACTIVE', true, t.id
FROM tenant t WHERE t.domain = 'default.musicschool.com'
ON CONFLICT (username) DO NOTHING;

-- Add roles for sample users
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'INSTRUCTOR'
FROM users u WHERE u.username = 'instructor1'
ON CONFLICT (user_id, role) DO NOTHING;

INSERT INTO user_roles (user_id, role)
SELECT u.id, 'STUDENT'
FROM users u WHERE u.username = 'student1'
ON CONFLICT (user_id, role) DO NOTHING;

-- Add some sample payments
INSERT INTO payments (student_id, amount, payment_date, payment_method, status, transaction_id, notes)
SELECT s.id, 120.00, '2024-01-15', 'CREDIT_CARD', 'COMPLETED', 'TXN001', 'Monthly payment for piano course'
FROM students s, tenant t
WHERE s.email = 'alice.johnson@email.com' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id
ON CONFLICT DO NOTHING;

INSERT INTO payments (student_id, amount, payment_date, payment_method, status, transaction_id, notes)
SELECT s.id, 100.00, '2024-01-16', 'BANK_TRANSFER', 'COMPLETED', 'TXN002', 'Monthly payment for guitar course'
FROM students s, tenant t
WHERE s.email = 'bob.smith@email.com' 
  AND t.domain = 'default.musicschool.com'
  AND s.tenant_id = t.id
ON CONFLICT DO NOTHING;

-- Add some sample schedules
INSERT INTO schedules (start_time, end_time, title, description, type, status, room, instructor_id, student_id, course_id, notes)
SELECT 
    '2024-01-22 16:00:00', '2024-01-22 17:00:00', 'Piano Lesson - Alice', 'Weekly piano lesson', 'LESSON', 'SCHEDULED', 'Room A', i.id, s.id, c.id, 'Regular weekly lesson'
FROM instructors i, students s, courses c, tenant t
WHERE i.email = 'john.smith@musicschool.com' 
  AND s.email = 'alice.johnson@email.com' 
  AND c.name = 'Piano for Beginners'
  AND t.domain = 'default.musicschool.com'
  AND i.tenant_id = t.id 
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;

INSERT INTO schedules (start_time, end_time, title, description, type, status, room, instructor_id, student_id, course_id, notes)
SELECT 
    '2024-01-23 17:00:00', '2024-01-23 18:00:00', 'Guitar Lesson - Bob', 'Weekly guitar lesson', 'LESSON', 'SCHEDULED', 'Room B', i.id, s.id, c.id, 'Regular weekly lesson'
FROM instructors i, students s, courses c, tenant t
WHERE i.email = 'sarah.johnson@musicschool.com' 
  AND s.email = 'bob.smith@email.com' 
  AND c.name = 'Guitar Fundamentals'
  AND t.domain = 'default.musicschool.com'
  AND i.tenant_id = t.id 
  AND s.tenant_id = t.id 
  AND c.tenant_id = t.id
ON CONFLICT DO NOTHING;
