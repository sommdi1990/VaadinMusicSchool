package com.musicschool.repository;

import com.musicschool.entity.Instructor;
import com.musicschool.entity.Schedule;
import com.musicschool.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Schedule entity.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.instructor = :instructor AND s.startTime BETWEEN :startTime AND :endTime")
    List<Schedule> findByInstructorAndTimeRange(@Param("instructor") Instructor instructor,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Schedule s WHERE s.student = :student AND s.startTime BETWEEN :startTime AND :endTime")
    List<Schedule> findByStudentAndTimeRange(@Param("student") Student student,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Schedule s WHERE s.room = :room AND s.startTime BETWEEN :startTime AND :endTime")
    List<Schedule> findByRoomAndTimeRange(@Param("room") String room,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
    
    List<Schedule> findByStartTimeBetweenOrderByStartTime(LocalDateTime startTime, LocalDateTime endTime);
    
    List<Schedule> findByStatus(Schedule.ScheduleStatus status);
    
    List<Schedule> findByType(Schedule.ScheduleType type);
    
    List<Schedule> findByRecurring(Boolean recurring);
    
    List<Schedule> findByParentScheduleId(Long parentScheduleId);
    
    @Query("SELECT s FROM Schedule s WHERE s.instructor = :instructor AND s.startTime BETWEEN :startTime AND :endTime")
    List<Schedule> findInstructorSchedule(@Param("instructor") Instructor instructor, 
                                        @Param("startTime") LocalDateTime startTime, 
                                        @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT s FROM Schedule s WHERE s.student = :student AND s.startTime BETWEEN :startTime AND :endTime")
    List<Schedule> findStudentSchedule(@Param("student") Student student, 
                                     @Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT s FROM Schedule s WHERE s.room = :room AND s.startTime BETWEEN :startTime AND :endTime")
    List<Schedule> findRoomSchedule(@Param("room") String room, 
                                  @Param("startTime") LocalDateTime startTime, 
                                  @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT s FROM Schedule s WHERE s.status = :status AND s.startTime < :currentTime")
    List<Schedule> findOverdueSchedules(@Param("status") Schedule.ScheduleStatus status, 
                                      @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT s FROM Schedule s WHERE s.recurring = true AND s.parentScheduleId IS NULL")
    List<Schedule> findRecurringTemplates();
    
    @Query("SELECT s FROM Schedule s WHERE s.parentScheduleId = :parentId")
    List<Schedule> findRecurringInstances(@Param("parentId") Long parentId);
}
