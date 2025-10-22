package com.musicschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a student's enrollment in a course.
 */
@Entity
@Table(name = "enrollments")
public class Enrollment extends BaseEntity {

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "tuition_paid", precision = 10, scale = 2)
    private BigDecimal tuitionPaid = BigDecimal.ZERO;

    @Column(name = "total_tuition", precision = 10, scale = 2)
    private BigDecimal totalTuition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(name = "notes", length = 1000)
    private String notes;

    // Constructors
    public Enrollment() {}

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
        this.totalTuition = course.getPrice();
    }

    // Getters and Setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public BigDecimal getTuitionPaid() {
        return tuitionPaid;
    }

    public void setTuitionPaid(BigDecimal tuitionPaid) {
        this.tuitionPaid = tuitionPaid;
    }

    public BigDecimal getTotalTuition() {
        return totalTuition;
    }

    public void setTotalTuition(BigDecimal totalTuition) {
        this.totalTuition = totalTuition;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper methods
    public BigDecimal getRemainingBalance() {
        if (totalTuition == null || tuitionPaid == null) {
            return BigDecimal.ZERO;
        }
        return totalTuition.subtract(tuitionPaid);
    }

    public boolean isPaidInFull() {
        return getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0;
    }

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED, SUSPENDED
    }
}
