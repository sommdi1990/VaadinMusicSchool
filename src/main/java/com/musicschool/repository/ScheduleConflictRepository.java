package com.musicschool.repository;

import com.musicschool.entity.ScheduleConflict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ScheduleConflict entity.
 */
@Repository
public interface ScheduleConflictRepository extends JpaRepository<ScheduleConflict, Long> {
    
    List<ScheduleConflict> findByResolved(Boolean resolved);
    
    List<ScheduleConflict> findByConflictType(ScheduleConflict.ConflictType conflictType);
    
    List<ScheduleConflict> findByScheduleId(Long scheduleId);
    
    List<ScheduleConflict> findByConflictingScheduleId(Long conflictingScheduleId);
    
    @Query("SELECT sc FROM ScheduleConflict sc WHERE sc.schedule.id = :scheduleId OR sc.conflictingSchedule.id = :scheduleId")
    List<ScheduleConflict> findConflictsForSchedule(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT sc FROM ScheduleConflict sc WHERE sc.resolved = false")
    List<ScheduleConflict> findUnresolvedConflicts();
    
    @Query("SELECT sc FROM ScheduleConflict sc WHERE sc.conflictType = :conflictType AND sc.resolved = false")
    List<ScheduleConflict> findUnresolvedConflictsByType(@Param("conflictType") ScheduleConflict.ConflictType conflictType);
    
    @Query("SELECT COUNT(sc) FROM ScheduleConflict sc WHERE sc.resolved = false")
    Long countUnresolvedConflicts();
    
    @Query("SELECT sc.conflictType, COUNT(sc) FROM ScheduleConflict sc WHERE sc.resolved = false GROUP BY sc.conflictType")
    List<Object[]> getConflictCountByType();
}
