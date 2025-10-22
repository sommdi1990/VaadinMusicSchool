package com.musicschool.repository;

import com.musicschool.entity.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Instructor entity operations.
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    /**
     * Find instructor by email.
     */
    Optional<Instructor> findByEmail(String email);

    /**
     * Find instructors by status.
     */
    List<Instructor> findByStatus(Instructor.InstructorStatus status);

    /**
     * Find instructors by specialization.
     */
    List<Instructor> findBySpecializationContainingIgnoreCase(String specialization);

    /**
     * Find instructors with pagination and search.
     */
    @Query("SELECT i FROM Instructor i WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(i.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.specialization) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Instructor> findInstructorsWithSearch(@Param("search") String search, Pageable pageable);

    /**
     * Count instructors by status.
     */
    long countByStatus(Instructor.InstructorStatus status);

    /**
     * Find active instructors.
     */
    @Query("SELECT i FROM Instructor i WHERE i.status = 'ACTIVE' ORDER BY i.lastName, i.firstName")
    List<Instructor> findActiveInstructors();
}
