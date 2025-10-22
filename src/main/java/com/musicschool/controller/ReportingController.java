package com.musicschool.controller;

import com.musicschool.entity.Report;
import com.musicschool.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * REST Controller for reporting and analytics endpoints.
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    /**
     * Generate student enrollment report
     */
    @GetMapping("/enrollment")
    public ResponseEntity<Map<String, Object>> getEnrollmentReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> report = reportingService.generateStudentEnrollmentReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate revenue analysis report
     */
    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> report = reportingService.generateRevenueReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate instructor performance report
     */
    @GetMapping("/instructor-performance")
    public ResponseEntity<Map<String, Object>> getInstructorPerformanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> report = reportingService.generateInstructorPerformanceReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate course attendance report
     */
    @GetMapping("/attendance")
    public ResponseEntity<Map<String, Object>> getAttendanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> report = reportingService.generateCourseAttendanceReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate financial summary report
     */
    @GetMapping("/financial-summary")
    public ResponseEntity<Map<String, Object>> getFinancialSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> report = reportingService.generateFinancialSummaryReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * Save a custom report
     */
    @PostMapping("/save")
    public ResponseEntity<Report> saveReport(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Report.ReportType type,
            @RequestBody Map<String, Object> data,
            @RequestParam String createdBy) {
        
        Report report = reportingService.saveReport(name, description, type, data, createdBy);
        return ResponseEntity.ok(report);
    }
}
