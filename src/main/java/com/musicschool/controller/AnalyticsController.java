package com.musicschool.controller;

import com.musicschool.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * REST Controller for analytics and metrics endpoints.
 */
@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Get analytics dashboard data
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> dashboard = analyticsService.getDashboardData(startDate, endDate);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get performance insights
     */
    @GetMapping("/insights")
    public ResponseEntity<Map<String, Object>> getPerformanceInsights() {
        Map<String, Object> insights = analyticsService.getPerformanceInsights();
        return ResponseEntity.ok(insights);
    }

    /**
     * Trigger daily metrics collection
     */
    @PostMapping("/collect-metrics")
    public ResponseEntity<String> collectDailyMetrics() {
        analyticsService.collectDailyMetrics();
        return ResponseEntity.ok("Metrics collection started");
    }
}
