package com.musicschool.repository;

import com.musicschool.entity.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Analytics entity.
 */
@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    
    List<Analytics> findByMetricType(Analytics.MetricType metricType);
    
    List<Analytics> findByMetricName(String metricName);
    
    List<Analytics> findByDateRecordedBetween(LocalDate startDate, LocalDate endDate);
    
    List<Analytics> findByMetricTypeAndDateRecordedBetween(Analytics.MetricType metricType, 
                                                          LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT a FROM Analytics a WHERE a.metricType = :metricType AND a.dateRecorded = :date")
    List<Analytics> findByMetricTypeAndDate(@Param("metricType") Analytics.MetricType metricType, 
                                           @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Analytics a WHERE a.category = :category AND a.dateRecorded BETWEEN :startDate AND :endDate")
    List<Analytics> findByCategoryAndDateRange(@Param("category") String category, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a.metricName, AVG(a.metricValue) FROM Analytics a WHERE a.metricType = :metricType GROUP BY a.metricName")
    List<Object[]> getAverageMetricsByType(@Param("metricType") Analytics.MetricType metricType);
}
