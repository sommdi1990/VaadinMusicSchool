package com.musicschool.service;

import com.musicschool.entity.Analytics;
import com.musicschool.entity.Student;
import com.musicschool.entity.Course;
import com.musicschool.entity.Instructor;
import com.musicschool.entity.Payment;
import com.musicschool.repository.AnalyticsRepository;
import com.musicschool.repository.StudentRepository;
import com.musicschool.repository.CourseRepository;
import com.musicschool.repository.InstructorRepository;
import com.musicschool.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for collecting and analyzing system metrics and analytics.
 */
@Service
@Transactional
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Collect daily analytics metrics
     */
    @Async
    public void collectDailyMetrics() {
        LocalDate today = LocalDate.now();
        
        // Revenue metrics
        collectRevenueMetrics(today);
        
        // Enrollment metrics
        collectEnrollmentMetrics(today);
        
        // Attendance metrics
        collectAttendanceMetrics(today);
        
        // Instructor performance metrics
        collectInstructorMetrics(today);
        
        // Course completion metrics
        collectCourseCompletionMetrics(today);
    }

    /**
     * Collect revenue analytics
     */
    private void collectRevenueMetrics(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        // Total daily revenue
        BigDecimal dailyRevenue = paymentRepository.findByProcessedAtBetween(startOfDay, endOfDay)
            .stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        saveMetric(Analytics.MetricType.REVENUE, "daily_revenue", dailyRevenue, date);
        
        // Revenue by course type
        Map<String, BigDecimal> revenueByCourse = paymentRepository.findByProcessedAtBetween(startOfDay, endOfDay)
            .stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED && p.getCourse() != null)
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getCourse().getInstrument().toString(),
                java.util.stream.Collectors.reducing(BigDecimal.ZERO, Payment::getAmount, BigDecimal::add)
            ));
        
        for (Map.Entry<String, BigDecimal> entry : revenueByCourse.entrySet()) {
            saveMetric(Analytics.MetricType.REVENUE, "revenue_by_instrument_" + entry.getKey(), 
                      entry.getValue(), date);
        }
    }

    /**
     * Collect enrollment analytics
     */
    private void collectEnrollmentMetrics(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        // Daily enrollments
        long dailyEnrollments = studentRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        saveMetric(Analytics.MetricType.ENROLLMENT, "daily_enrollments", 
                  new BigDecimal(dailyEnrollments), date);
        
        // Enrollments by level
        Map<String, Long> enrollmentsByLevel = studentRepository.findAll()
            .stream()
            .filter(s -> s.getCreatedAt().toLocalDate().equals(date))
            .collect(java.util.stream.Collectors.groupingBy(
                s -> s.getLevel().toString(),
                java.util.stream.Collectors.counting()
            ));
        
        for (Map.Entry<String, Long> entry : enrollmentsByLevel.entrySet()) {
            saveMetric(Analytics.MetricType.ENROLLMENT, "enrollments_by_level_" + entry.getKey(), 
                      new BigDecimal(entry.getValue()), date);
        }
    }

    /**
     * Collect attendance analytics
     */
    private void collectAttendanceMetrics(LocalDate date) {
        // This would typically integrate with attendance tracking system
        // For now, we'll use mock data
        
        // Average attendance rate
        BigDecimal attendanceRate = new BigDecimal("0.85");
        saveMetric(Analytics.MetricType.ATTENDANCE, "average_attendance_rate", attendanceRate, date);
        
        // Peak hours (mock data)
        saveMetric(Analytics.MetricType.PEAK_HOURS, "peak_hour_14_00", new BigDecimal("1"), date);
        saveMetric(Analytics.MetricType.PEAK_HOURS, "peak_hour_15_00", new BigDecimal("1"), date);
        saveMetric(Analytics.MetricType.PEAK_HOURS, "peak_hour_16_00", new BigDecimal("1"), date);
    }

    /**
     * Collect instructor performance metrics
     */
    private void collectInstructorMetrics(LocalDate date) {
        List<Instructor> instructors = instructorRepository.findAll();
        
        for (Instructor instructor : instructors) {
            // Hours worked (mock calculation)
            BigDecimal hoursWorked = new BigDecimal(instructor.getCourses().size() * 2); // 2 hours per course
            saveMetric(Analytics.MetricType.INSTRUCTOR_HOURS, 
                      "instructor_hours_" + instructor.getId(), hoursWorked, date);
            
            // Student satisfaction (mock data)
            BigDecimal satisfaction = new BigDecimal("4.5");
            saveMetric(Analytics.MetricType.STUDENT_SATISFACTION, 
                      "instructor_satisfaction_" + instructor.getId(), satisfaction, date);
        }
    }

    /**
     * Collect course completion metrics
     */
    private void collectCourseCompletionMetrics(LocalDate date) {
        List<Course> courses = courseRepository.findAll();
        
        for (Course course : courses) {
            // Course completion rate (mock calculation)
            BigDecimal completionRate = new BigDecimal("0.78");
            saveMetric(Analytics.MetricType.COURSE_COMPLETION_RATE, 
                      "course_completion_" + course.getId(), completionRate, date);
            
            // Average course duration
            BigDecimal avgDuration = new BigDecimal(course.getDurationMinutes());
            saveMetric(Analytics.MetricType.AVERAGE_COURSE_DURATION, 
                      "avg_duration_" + course.getId(), avgDuration, date);
        }
    }

    /**
     * Get analytics dashboard data
     */
    public Map<String, Object> getDashboardData(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Revenue trends
        List<Analytics> revenueData = analyticsRepository.findByMetricTypeAndDateRecordedBetween(
            Analytics.MetricType.REVENUE, startDate, endDate);
        dashboard.put("revenueTrends", revenueData);
        
        // Enrollment trends
        List<Analytics> enrollmentData = analyticsRepository.findByMetricTypeAndDateRecordedBetween(
            Analytics.MetricType.ENROLLMENT, startDate, endDate);
        dashboard.put("enrollmentTrends", enrollmentData);
        
        // Attendance trends
        List<Analytics> attendanceData = analyticsRepository.findByMetricTypeAndDateRecordedBetween(
            Analytics.MetricType.ATTENDANCE, startDate, endDate);
        dashboard.put("attendanceTrends", attendanceData);
        
        // Key metrics
        dashboard.put("totalStudents", studentRepository.count());
        dashboard.put("totalCourses", courseRepository.count());
        dashboard.put("totalInstructors", instructorRepository.count());
        dashboard.put("activeCourses", courseRepository.findAll().stream()
            .filter(c -> c.getStatus() == Course.CourseStatus.ACTIVE)
            .count());
        
        return dashboard;
    }

    /**
     * Get performance insights
     */
    public Map<String, Object> getPerformanceInsights() {
        Map<String, Object> insights = new HashMap<>();
        
        // Top performing instructors
        List<Instructor> topInstructors = instructorRepository.findAll().stream()
            .sorted((i1, i2) -> Integer.compare(i2.getCourses().size(), i1.getCourses().size()))
            .limit(5)
            .collect(java.util.stream.Collectors.toList());
        insights.put("topInstructors", topInstructors);
        
        // Most popular courses
        List<Course> popularCourses = courseRepository.findAll().stream()
            .sorted((c1, c2) -> Integer.compare(c2.getEnrollments().size(), c1.getEnrollments().size()))
            .limit(5)
            .collect(java.util.stream.Collectors.toList());
        insights.put("popularCourses", popularCourses);
        
        // Revenue insights
        BigDecimal totalRevenue = paymentRepository.findAll().stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        insights.put("totalRevenue", totalRevenue);
        
        return insights;
    }

    /**
     * Save a metric to the database
     */
    private void saveMetric(Analytics.MetricType metricType, String metricName, 
                           BigDecimal metricValue, LocalDate date) {
        Analytics analytics = new Analytics(metricType, metricName, metricValue);
        analytics.setDateRecorded(date);
        analytics.setTimestamp(LocalDateTime.now());
        analyticsRepository.save(analytics);
    }
}
