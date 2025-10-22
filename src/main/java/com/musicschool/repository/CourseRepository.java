package com.musicschool.repository;

import com.musicschool.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Course entity operations.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Find courses by instructor.
     */
    List<Course> findByInstructorId(Long instructorId);

    /**
     * Find courses by instrument.
     */
    List<Course> findByInstrument(Course.Instrument instrument);

    /**
     * Find courses by level.
     */
    List<Course> findByLevel(Course.CourseLevel level);

    /**
     * Find courses by status.
     */
    List<Course> findByStatus(Course.CourseStatus status);

    /**
     * Find active courses.
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE' ORDER BY c.name")
    List<Course> findActiveCourses();

    /**
     * Find courses with pagination and search.
     */
    @Query("SELECT c FROM Course c WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.instructor.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.instructor.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Course> findCoursesWithSearch(@Param("search") String search, Pageable pageable);

    /**
     * Find courses starting after a specific date.
     */
    @Query("SELECT c FROM Course c WHERE c.startDate >= :startDate ORDER BY c.startDate")
    List<Course> findCoursesStartingAfter(@Param("startDate") LocalDate startDate);

    /**
     * Find courses by instructor and status.
     */
    List<Course> findByInstructorIdAndStatus(Long instructorId, Course.CourseStatus status);

    /**
     * Count courses by status.
     */
    long countByStatus(Course.CourseStatus status);

    /**
     * Find courses with available spots.
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE' AND " +
           "(SELECT COUNT(e) FROM Enrollment e WHERE e.course = c AND e.status = 'ACTIVE') < c.maxStudents")
    List<Course> findCoursesWithAvailableSpots();
}
