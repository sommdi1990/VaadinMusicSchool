package com.musicschool.repository;

import com.musicschool.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Student entity operations.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find student by email.
     */
    Optional<Student> findByEmail(String email);

    /**
     * Find students by level.
     */
    List<Student> findByLevel(Student.StudentLevel level);

    /**
     * Find students by first name or last name containing the given text.
     */
    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find students with pagination and search.
     */
    @Query("SELECT s FROM Student s WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Student> findStudentsWithSearch(@Param("search") String search, Pageable pageable);

    /**
     * Count students by level.
     */
    long countByLevel(Student.StudentLevel level);

    /**
     * Find students enrolled in a specific course.
     */
    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.course.id = :courseId")
    List<Student> findByCourseId(@Param("courseId") Long courseId);
}
