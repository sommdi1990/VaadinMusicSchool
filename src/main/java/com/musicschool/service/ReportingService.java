package com.musicschool.service;

import com.musicschool.entity.Report;
import com.musicschool.entity.Student;
import com.musicschool.entity.Course;
import com.musicschool.entity.Instructor;
import com.musicschool.entity.Payment;
import com.musicschool.repository.ReportRepository;
import com.musicschool.repository.StudentRepository;
import com.musicschool.repository.CourseRepository;
import com.musicschool.repository.InstructorRepository;
import com.musicschool.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating advanced reports and analytics.
 */
@Service
@Transactional
public class ReportingService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Generate student enrollment report
     */
    public Map<String, Object> generateStudentEnrollmentReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // Total enrollments
        long totalEnrollments = studentRepository.countByCreatedAtBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        report.put("totalEnrollments", totalEnrollments);
        
        // Enrollments by level
        Map<String, Long> enrollmentsByLevel = studentRepository.findAll()
            .stream()
            .filter(s -> s.getCreatedAt().toLocalDate().isAfter(startDate.minusDays(1)) && 
                       s.getCreatedAt().toLocalDate().isBefore(endDate.plusDays(1)))
            .collect(Collectors.groupingBy(
                s -> s.getLevel().toString(),
                Collectors.counting()
            ));
        report.put("enrollmentsByLevel", enrollmentsByLevel);
        
        // Monthly enrollment trends
        Map<String, Long> monthlyTrends = new HashMap<>();
        LocalDate current = startDate.withDayOfMonth(1);
        while (!current.isAfter(endDate)) {
            LocalDate monthStart = current;
            LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());
            long count = studentRepository.countByCreatedAtBetween(
                monthStart.atStartOfDay(), 
                monthEnd.atTime(23, 59, 59)
            );
            monthlyTrends.put(current.toString(), count);
            current = current.plusMonths(1);
        }
        report.put("monthlyTrends", monthlyTrends);
        
        return report;
    }

    /**
     * Generate revenue analysis report
     */
    public Map<String, Object> generateRevenueReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // Total revenue
        BigDecimal totalRevenue = paymentRepository.findByProcessedAtBetween(
            startDate.atStartOfDay(), 
            endDate.atTime(23, 59, 59)
        ).stream()
        .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
        .map(Payment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("totalRevenue", totalRevenue);
        
        // Revenue by course
        Map<String, BigDecimal> revenueByCourse = paymentRepository.findByProcessedAtBetween(
            startDate.atStartOfDay(), 
            endDate.atTime(23, 59, 59)
        ).stream()
        .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED && p.getCourse() != null)
        .collect(Collectors.groupingBy(
            p -> p.getCourse().getName(),
            Collectors.reducing(BigDecimal.ZERO, Payment::getAmount, BigDecimal::add)
        ));
        report.put("revenueByCourse", revenueByCourse);
        
        // Monthly revenue trends
        Map<String, BigDecimal> monthlyRevenue = new HashMap<>();
        LocalDate current = startDate.withDayOfMonth(1);
        while (!current.isAfter(endDate)) {
            LocalDate monthStart = current;
            LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());
            BigDecimal monthRevenue = paymentRepository.findByProcessedAtBetween(
                monthStart.atStartOfDay(), 
                monthEnd.atTime(23, 59, 59)
            ).stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            monthlyRevenue.put(current.toString(), monthRevenue);
            current = current.plusMonths(1);
        }
        report.put("monthlyRevenue", monthlyRevenue);
        
        return report;
    }

    /**
     * Generate instructor performance report
     */
    public Map<String, Object> generateInstructorPerformanceReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        List<Instructor> instructors = instructorRepository.findAll();
        List<Map<String, Object>> instructorStats = new ArrayList<>();
        
        for (Instructor instructor : instructors) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("instructorName", instructor.getFullName());
            stats.put("totalCourses", instructor.getCourses().size());
            stats.put("activeCourses", instructor.getCourses().stream()
                .filter(c -> c.getStatus() == Course.CourseStatus.ACTIVE)
                .count());
            stats.put("totalStudents", instructor.getCourses().stream()
                .mapToInt(c -> c.getEnrollments().size())
                .sum());
            stats.put("averageRating", calculateAverageRating(instructor));
            instructorStats.add(stats);
        }
        
        report.put("instructorStats", instructorStats);
        return report;
    }

    /**
     * Generate course attendance report
     */
    public Map<String, Object> generateCourseAttendanceReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        List<Course> courses = courseRepository.findAll();
        List<Map<String, Object>> courseStats = new ArrayList<>();
        
        for (Course course : courses) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("courseName", course.getName());
            stats.put("totalEnrollments", course.getEnrollments().size());
            stats.put("attendanceRate", calculateAttendanceRate(course));
            stats.put("completionRate", calculateCompletionRate(course));
            courseStats.add(stats);
        }
        
        report.put("courseStats", courseStats);
        return report;
    }

    /**
     * Generate financial summary report
     */
    public Map<String, Object> generateFinancialSummaryReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // Total revenue
        BigDecimal totalRevenue = paymentRepository.findByProcessedAtBetween(
            startDate.atStartOfDay(), 
            endDate.atTime(23, 59, 59)
        ).stream()
        .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
        .map(Payment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Total refunds
        BigDecimal totalRefunds = paymentRepository.findByProcessedAtBetween(
            startDate.atStartOfDay(), 
            endDate.atTime(23, 59, 59)
        ).stream()
        .filter(p -> p.getStatus() == Payment.PaymentStatus.REFUNDED)
        .map(Payment::getRefundAmount)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Net revenue
        BigDecimal netRevenue = totalRevenue.subtract(totalRefunds);
        
        report.put("totalRevenue", totalRevenue);
        report.put("totalRefunds", totalRefunds);
        report.put("netRevenue", netRevenue);
        report.put("refundRate", totalRevenue.compareTo(BigDecimal.ZERO) > 0 ? 
            totalRefunds.divide(totalRevenue, 4, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
        
        return report;
    }

    /**
     * Save report to database
     */
    public Report saveReport(String name, String description, Report.ReportType type, 
                           Map<String, Object> data, String createdBy) {
        Report report = new Report(name, type, createdBy);
        report.setDescription(description);
        report.setParameters(convertMapToJson(data));
        report.setStatus(Report.ReportStatus.COMPLETED);
        return reportRepository.save(report);
    }

    // Helper methods
    private double calculateAverageRating(Instructor instructor) {
        // This would typically come from a rating/review system
        // For now, return a mock value
        return 4.5;
    }

    private double calculateAttendanceRate(Course course) {
        // This would typically come from attendance records
        // For now, return a mock value
        return 0.85;
    }

    private double calculateCompletionRate(Course course) {
        // This would typically come from completion records
        // For now, return a mock value
        return 0.78;
    }

    private String convertMapToJson(Map<String, Object> data) {
        // Simple JSON conversion - in production, use a proper JSON library
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }
}
