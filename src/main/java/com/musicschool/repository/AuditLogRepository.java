package com.musicschool.repository;

import com.musicschool.entity.AuditLog;
import com.musicschool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for AuditLog entity.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByUser(User user);
    
    List<AuditLog> findByAction(String action);
    
    List<AuditLog> findByEntityType(String entityType);
    
    List<AuditLog> findBySeverity(AuditLog.Severity severity);
    
    List<AuditLog> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT al FROM AuditLog al WHERE al.user = :user AND al.timestamp BETWEEN :startTime AND :endTime")
    List<AuditLog> findByUserAndTimeRange(@Param("user") User user, 
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType AND al.entityId = :entityId")
    List<AuditLog> findByEntity(@Param("entityType") String entityType, 
                               @Param("entityId") Long entityId);
    
    @Query("SELECT al FROM AuditLog al WHERE al.severity = :severity AND al.timestamp BETWEEN :startTime AND :endTime")
    List<AuditLog> findBySeverityAndTimeRange(@Param("severity") AuditLog.Severity severity, 
                                             @Param("startTime") LocalDateTime startTime, 
                                             @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT al FROM AuditLog al WHERE al.ipAddress = :ipAddress AND al.timestamp BETWEEN :startTime AND :endTime")
    List<AuditLog> findByIpAddressAndTimeRange(@Param("ipAddress") String ipAddress, 
                                             @Param("startTime") LocalDateTime startTime, 
                                             @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT al FROM AuditLog al WHERE al.sessionId = :sessionId")
    List<AuditLog> findBySessionId(@Param("sessionId") String sessionId);
    
    @Query("SELECT COUNT(al) FROM AuditLog al WHERE al.severity = :severity AND al.timestamp BETWEEN :startTime AND :endTime")
    Long countBySeverityAndTimeRange(@Param("severity") AuditLog.Severity severity, 
                                   @Param("startTime") LocalDateTime startTime, 
                                   @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT al.action, COUNT(al) FROM AuditLog al WHERE al.timestamp BETWEEN :startTime AND :endTime GROUP BY al.action")
    List<Object[]> getActionCounts(@Param("startTime") LocalDateTime startTime, 
                                  @Param("endTime") LocalDateTime endTime);
}
