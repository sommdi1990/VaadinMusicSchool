package com.musicschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity for storing analytics data and metrics.
 */
@Entity
@Table(name = "analytics")
public class Analytics extends BaseEntity {

    @NotNull(message = "Metric type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false)
    private MetricType metricType;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value", precision = 15, scale = 4)
    private BigDecimal metricValue;

    @Column(name = "date_recorded")
    private LocalDate dateRecorded;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "dimensions", columnDefinition = "TEXT")
    private String dimensions; // JSON string for additional dimensions

    @Column(name = "category")
    private String category;

    // Constructors
    public Analytics() {}

    public Analytics(MetricType metricType, String metricName, BigDecimal metricValue) {
        this.metricType = metricType;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.dateRecorded = LocalDate.now();
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public BigDecimal getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(BigDecimal metricValue) {
        this.metricValue = metricValue;
    }

    public LocalDate getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(LocalDate dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public enum MetricType {
        REVENUE,
        ENROLLMENT,
        ATTENDANCE,
        INSTRUCTOR_HOURS,
        STUDENT_SATISFACTION,
        COURSE_COMPLETION_RATE,
        RETENTION_RATE,
        AVERAGE_COURSE_DURATION,
        PEAK_HOURS,
        ROOM_UTILIZATION
    }
}
