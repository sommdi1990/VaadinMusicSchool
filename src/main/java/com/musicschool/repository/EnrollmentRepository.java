package com.musicschool.repository;

import com.musicschool.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Enrollment entity operations.
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Find enrollments by student.
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * Find enrollments by course.
     */
    List<Enrollment> findByCourseId(Long courseId);

    /**
     * Find enrollments by status.
     */
    List<Enrollment> findByStatus(Enrollment.EnrollmentStatus status);

    /**
     * Find active enrollments for a student.
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByStudent(@Param("studentId") Long studentId);

    /**
     * Find active enrollments for a course.
     */
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByCourse(@Param("courseId") Long courseId);

    /**
     * Find enrollments with pagination and search.
     */
    @Query("SELECT e FROM Enrollment e WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(e.student.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.student.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.course.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Enrollment> findEnrollmentsWithSearch(@Param("search") String search, Pageable pageable);

    /**
     * Find enrollments by date range.
     */
    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentDate BETWEEN :startDate AND :endDate")
    List<Enrollment> findByEnrollmentDateBetween(@Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);

    /**
     * Find enrollments with outstanding balance.
     */
    @Query("SELECT e FROM Enrollment e WHERE e.status = 'ACTIVE' AND e.totalTuition > e.tuitionPaid")
    List<Enrollment> findEnrollmentsWithOutstandingBalance();

    /**
     * Calculate total revenue from enrollments.
     */
    @Query("SELECT COALESCE(SUM(e.tuitionPaid), 0) FROM Enrollment e WHERE e.status = 'ACTIVE'")
    BigDecimal calculateTotalRevenue();

    /**
     * Calculate outstanding balance.
     */
    @Query("SELECT COALESCE(SUM(e.totalTuition - e.tuitionPaid), 0) FROM Enrollment e WHERE e.status = 'ACTIVE'")
    BigDecimal calculateOutstandingBalance();

    /**
     * Count enrollments by status.
     */
    long countByStatus(Enrollment.EnrollmentStatus status);

    /**
     * Check if student is already enrolled in a course.
     */
    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId AND e.status = 'ACTIVE'")
    boolean existsByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
