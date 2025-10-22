package com.musicschool.controller;

import com.musicschool.entity.Schedule;
import com.musicschool.entity.ScheduleConflict;
import com.musicschool.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for advanced scheduling endpoints.
 */
@RestController
@RequestMapping("/api/scheduling")
@CrossOrigin(origins = "*")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    /**
     * Create a new schedule entry
     */
    @PostMapping("/schedule")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule createdSchedule = schedulingService.createSchedule(schedule);
        return ResponseEntity.ok(createdSchedule);
    }

    /**
     * Create recurring schedule entries
     */
    @PostMapping("/recurring")
    public ResponseEntity<List<Schedule>> createRecurringSchedule(
            @RequestBody Schedule template,
            @RequestBody SchedulingService.RecurrencePattern pattern) {
        
        List<Schedule> schedules = schedulingService.createRecurringSchedule(template, pattern);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Detect scheduling conflicts
     */
    @PostMapping("/detect-conflicts")
    public ResponseEntity<List<ScheduleConflict>> detectConflicts(@RequestBody Schedule schedule) {
        List<ScheduleConflict> conflicts = schedulingService.detectConflicts(schedule);
        return ResponseEntity.ok(conflicts);
    }

    /**
     * Resolve a scheduling conflict
     */
    @PostMapping("/resolve-conflict/{conflictId}")
    public ResponseEntity<ScheduleConflict> resolveConflict(
            @PathVariable Long conflictId,
            @RequestParam String resolutionNotes) {
        
        ScheduleConflict conflict = schedulingService.resolveConflict(conflictId, resolutionNotes);
        return ResponseEntity.ok(conflict);
    }

    /**
     * Get instructor availability
     */
    @GetMapping("/instructor/{instructorId}/availability")
    public ResponseEntity<List<SchedulingService.TimeSlot>> getInstructorAvailability(
            @PathVariable Long instructorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<SchedulingService.TimeSlot> availability = schedulingService.getInstructorAvailability(
            instructorId, startDate, endDate);
        return ResponseEntity.ok(availability);
    }

    /**
     * Get room availability
     */
    @GetMapping("/room/{room}/availability")
    public ResponseEntity<List<SchedulingService.TimeSlot>> getRoomAvailability(
            @PathVariable String room,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<SchedulingService.TimeSlot> availability = schedulingService.getRoomAvailability(
            room, startDate, endDate);
        return ResponseEntity.ok(availability);
    }

    /**
     * Get schedule for a specific date range
     */
    @GetMapping("/schedule")
    public ResponseEntity<List<Schedule>> getScheduleForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Schedule> schedules = schedulingService.getScheduleForDateRange(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get instructor schedule
     */
    @GetMapping("/instructor/{instructorId}/schedule")
    public ResponseEntity<List<Schedule>> getInstructorSchedule(
            @PathVariable Long instructorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Schedule> schedules = schedulingService.getInstructorSchedule(
            instructorId, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get student schedule
     */
    @GetMapping("/student/{studentId}/schedule")
    public ResponseEntity<List<Schedule>> getStudentSchedule(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Schedule> schedules = schedulingService.getStudentSchedule(
            studentId, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Cancel a schedule entry
     */
    @PostMapping("/schedule/{scheduleId}/cancel")
    public ResponseEntity<Schedule> cancelSchedule(
            @PathVariable Long scheduleId,
            @RequestParam String reason) {
        
        Schedule schedule = schedulingService.cancelSchedule(scheduleId, reason);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Reschedule an entry
     */
    @PostMapping("/schedule/{scheduleId}/reschedule")
    public ResponseEntity<Schedule> reschedule(
            @PathVariable Long scheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newStartTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEndTime) {
        
        Schedule schedule = schedulingService.reschedule(scheduleId, newStartTime, newEndTime);
        return ResponseEntity.ok(schedule);
    }
}
