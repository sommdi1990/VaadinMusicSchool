package com.musicschool.repository;

import com.musicschool.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Report entity.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByType(Report.ReportType type);
    
    List<Report> findByStatus(Report.ReportStatus status);
    
    List<Report> findByCreatedBy(String createdBy);
    
    List<Report> findByGeneratedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r FROM Report r WHERE r.type = :type AND r.generatedAt BETWEEN :startDate AND :endDate")
    List<Report> findByTypeAndDateRange(@Param("type") Report.ReportType type, 
                                       @Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT r FROM Report r WHERE r.status = :status AND r.generatedAt < :cutoffDate")
    List<Report> findOldPendingReports(@Param("status") Report.ReportStatus status, 
                                      @Param("cutoffDate") LocalDateTime cutoffDate);
}
