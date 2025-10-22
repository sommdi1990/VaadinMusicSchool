package com.musicschool.service;

import com.musicschool.entity.Course;
import com.musicschool.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Course entity operations.
 */
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Save a course.
     */
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Find course by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    /**
     * Find all courses.
     */
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    /**
     * Find courses with pagination and search.
     */
    @Transactional(readOnly = true)
    public Page<Course> findCoursesWithSearch(String search, Pageable pageable) {
        return courseRepository.findCoursesWithSearch(search, pageable);
    }

    /**
     * Find active courses.
     */
    @Transactional(readOnly = true)
    public List<Course> findActiveCourses() {
        return courseRepository.findActiveCourses();
    }

    /**
     * Find courses by instructor.
     */
    @Transactional(readOnly = true)
    public List<Course> findByInstructorId(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    /**
     * Find courses by instrument.
     */
    @Transactional(readOnly = true)
    public List<Course> findByInstrument(Course.Instrument instrument) {
        return courseRepository.findByInstrument(instrument);
    }

    /**
     * Find courses by level.
     */
    @Transactional(readOnly = true)
    public List<Course> findByLevel(Course.CourseLevel level) {
        return courseRepository.findByLevel(level);
    }

    /**
     * Find courses by status.
     */
    @Transactional(readOnly = true)
    public List<Course> findByStatus(Course.CourseStatus status) {
        return courseRepository.findByStatus(status);
    }

    /**
     * Find courses starting after a specific date.
     */
    @Transactional(readOnly = true)
    public List<Course> findCoursesStartingAfter(LocalDate startDate) {
        return courseRepository.findCoursesStartingAfter(startDate);
    }

    /**
     * Find courses with available spots.
     */
    @Transactional(readOnly = true)
    public List<Course> findCoursesWithAvailableSpots() {
        return courseRepository.findCoursesWithAvailableSpots();
    }

    /**
     * Delete a course.
     */
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Count courses by status.
     */
    @Transactional(readOnly = true)
    public long countByStatus(Course.CourseStatus status) {
        return courseRepository.countByStatus(status);
    }

    /**
     * Get total number of courses.
     */
    @Transactional(readOnly = true)
    public long count() {
        return courseRepository.count();
    }
}
