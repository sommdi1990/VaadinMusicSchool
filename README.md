# Student administration with Vaadin and Spring

A music-school application combines a Vaadin student grid and forms with Spring services and relational migrations. The student view supports name-based search and delegates writes to the student service.

A search refreshes the grid through findByNameContaining. Form submission passes a student to the service for saving. Read this path beside the initial tenant schema before moving on to scheduling, payments and reporting, which have separate service boundaries.

- [StudentsView.java](src/main/java/com/musicschool/ui/StudentsView.java)
- [StudentService.java](src/main/java/com/musicschool/service/StudentService.java)
- [V1__init.sql](src/main/resources/db/migration/V1__init.sql)
- [SchedulingService.java](src/main/java/com/musicschool/service/SchedulingService.java)

## 🗄️ Database Schema

**Database migrations are managed by Flyway. Schema is NOT created automatically; use migration scripts in `src/main/resources/db/migration/` to modify structure.** [Updated for Flyway]

### Core Entities
- **Students**: Personal information, contact details, skill level
- **Instructors**: Professional profiles, specializations, rates
- **Courses**: Course details, scheduling, capacity
- **Enrollments**: Student-course relationships, payments

### Key Relationships
- Students can enroll in multiple courses
- Instructors can teach multiple courses
- Each enrollment tracks payment status
- Audit fields track creation and modification times
