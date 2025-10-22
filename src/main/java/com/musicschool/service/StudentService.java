package com.musicschool.service;

import com.musicschool.entity.Student;
import com.musicschool.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Student entity operations.
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Save a student.
     */
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Find student by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Find all students.
     */
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Find students with pagination and search.
     */
    @Transactional(readOnly = true)
    public Page<Student> findStudentsWithSearch(String search, Pageable pageable) {
        return studentRepository.findStudentsWithSearch(search, pageable);
    }

    /**
     * Find student by email.
     */
    @Transactional(readOnly = true)
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    /**
     * Find students by level.
     */
    @Transactional(readOnly = true)
    public List<Student> findByLevel(Student.StudentLevel level) {
        return studentRepository.findByLevel(level);
    }

    /**
     * Find students by name.
     */
    @Transactional(readOnly = true)
    public List<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Find students enrolled in a specific course.
     */
    @Transactional(readOnly = true)
    public List<Student> findByCourseId(Long courseId) {
        return studentRepository.findByCourseId(courseId);
    }

    /**
     * Delete a student.
     */
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * Count students by level.
     */
    @Transactional(readOnly = true)
    public long countByLevel(Student.StudentLevel level) {
        return studentRepository.countByLevel(level);
    }

    /**
     * Get total number of students.
     */
    @Transactional(readOnly = true)
    public long count() {
        return studentRepository.count();
    }
}
