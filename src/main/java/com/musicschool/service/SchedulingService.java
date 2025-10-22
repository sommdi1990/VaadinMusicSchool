package com.musicschool.service;

import com.musicschool.entity.Schedule;
import com.musicschool.entity.ScheduleConflict;
import com.musicschool.entity.Student;
import com.musicschool.entity.Instructor;
import com.musicschool.entity.Course;
import com.musicschool.repository.ScheduleRepository;
import com.musicschool.repository.ScheduleConflictRepository;
import com.musicschool.repository.StudentRepository;
import com.musicschool.repository.InstructorRepository;
import com.musicschool.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for advanced scheduling features including conflict detection and recurring schedules.
 */
@Service
@Transactional
public class SchedulingService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleConflictRepository scheduleConflictRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Create a new schedule entry
     */
    public Schedule createSchedule(Schedule schedule) {
        // Check for conflicts before saving
        List<ScheduleConflict> conflicts = detectConflicts(schedule);
        if (!conflicts.isEmpty()) {
            // Save conflicts for review
            scheduleConflictRepository.saveAll(conflicts);
        }
        
        return scheduleRepository.save(schedule);
    }

    /**
     * Create recurring schedule entries
     */
    public List<Schedule> createRecurringSchedule(Schedule template, RecurrencePattern pattern) {
        List<Schedule> schedules = new ArrayList<>();
        LocalDateTime currentStart = template.getStartTime();
        LocalDateTime currentEnd = template.getEndTime();
        
        for (int i = 0; i < pattern.getOccurrences(); i++) {
            Schedule schedule = new Schedule(currentStart, currentEnd, template.getType());
            schedule.setTitle(template.getTitle());
            schedule.setDescription(template.getDescription());
            schedule.setInstructor(template.getInstructor());
            schedule.setStudent(template.getStudent());
            schedule.setCourse(template.getCourse());
            schedule.setRoom(template.getRoom());
            schedule.setRecurring(true);
            schedule.setParentScheduleId(template.getId());
            schedule.setRecurrencePattern(pattern.toJson());
            
            // Check for conflicts
            List<ScheduleConflict> conflicts = detectConflicts(schedule);
            if (conflicts.isEmpty()) {
                schedules.add(scheduleRepository.save(schedule));
            }
            
            // Calculate next occurrence
            currentStart = calculateNextOccurrence(currentStart, pattern);
            currentEnd = calculateNextOccurrence(currentEnd, pattern);
        }
        
        return schedules;
    }

    /**
     * Detect scheduling conflicts
     */
    public List<ScheduleConflict> detectConflicts(Schedule schedule) {
        List<ScheduleConflict> conflicts = new ArrayList<>();
        
        // Check instructor conflicts
        List<Schedule> instructorConflicts = scheduleRepository.findByInstructorAndTimeRange(
            schedule.getInstructor(), schedule.getStartTime(), schedule.getEndTime());
        for (Schedule conflict : instructorConflicts) {
            if (!conflict.getId().equals(schedule.getId())) {
                ScheduleConflict scheduleConflict = new ScheduleConflict(
                    schedule, conflict, ScheduleConflict.ConflictType.INSTRUCTOR_DOUBLE_BOOKING);
                scheduleConflict.setDescription("Instructor " + schedule.getInstructor().getFullName() + 
                    " is already scheduled at this time");
                conflicts.add(scheduleConflict);
            }
        }
        
        // Check student conflicts
        if (schedule.getStudent() != null) {
            List<Schedule> studentConflicts = scheduleRepository.findByStudentAndTimeRange(
                schedule.getStudent(), schedule.getStartTime(), schedule.getEndTime());
            for (Schedule conflict : studentConflicts) {
                if (!conflict.getId().equals(schedule.getId())) {
                    ScheduleConflict scheduleConflict = new ScheduleConflict(
                        schedule, conflict, ScheduleConflict.ConflictType.STUDENT_DOUBLE_BOOKING);
                    scheduleConflict.setDescription("Student " + schedule.getStudent().getFullName() + 
                        " is already scheduled at this time");
                    conflicts.add(scheduleConflict);
                }
            }
        }
        
        // Check room conflicts
        if (schedule.getRoom() != null) {
            List<Schedule> roomConflicts = scheduleRepository.findByRoomAndTimeRange(
                schedule.getRoom(), schedule.getStartTime(), schedule.getEndTime());
            for (Schedule conflict : roomConflicts) {
                if (!conflict.getId().equals(schedule.getId())) {
                    ScheduleConflict scheduleConflict = new ScheduleConflict(
                        schedule, conflict, ScheduleConflict.ConflictType.ROOM_DOUBLE_BOOKING);
                    scheduleConflict.setDescription("Room " + schedule.getRoom() + 
                        " is already booked at this time");
                    conflicts.add(scheduleConflict);
                }
            }
        }
        
        return conflicts;
    }

    /**
     * Resolve a scheduling conflict
     */
    public ScheduleConflict resolveConflict(Long conflictId, String resolutionNotes) {
        ScheduleConflict conflict = scheduleConflictRepository.findById(conflictId)
            .orElseThrow(() -> new RuntimeException("Conflict not found"));
        
        conflict.setResolved(true);
        conflict.setResolvedAt(LocalDateTime.now());
        conflict.setResolutionNotes(resolutionNotes);
        
        return scheduleConflictRepository.save(conflict);
    }

    /**
     * Get instructor availability
     */
    public List<TimeSlot> getInstructorAvailability(Long instructorId, LocalDateTime startDate, LocalDateTime endDate) {
        Instructor instructor = instructorRepository.findById(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));
        
        List<Schedule> existingSchedules = scheduleRepository.findByInstructorAndTimeRange(
            instructor, startDate, endDate);
        
        List<TimeSlot> availability = new ArrayList<>();
        LocalDateTime current = startDate;
        
        while (current.isBefore(endDate)) {
            LocalDateTime slotEnd = current.plusHours(1);
            
            final LocalDateTime slotStart = current;
            boolean isAvailable = existingSchedules.stream()
                .noneMatch(s -> isTimeOverlapping(slotStart, slotEnd, s.getStartTime(), s.getEndTime()));
            
            if (isAvailable) {
                availability.add(new TimeSlot(slotStart, slotEnd));
            }
            
            current = current.plusHours(1);
        }
        
        return availability;
    }

    /**
     * Get room availability
     */
    public List<TimeSlot> getRoomAvailability(String room, LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> existingSchedules = scheduleRepository.findByRoomAndTimeRange(room, startDate, endDate);
        
        List<TimeSlot> availability = new ArrayList<>();
        LocalDateTime current = startDate;
        
        while (current.isBefore(endDate)) {
            LocalDateTime slotEnd = current.plusHours(1);
            
            final LocalDateTime slotStart = current;
            boolean isAvailable = existingSchedules.stream()
                .noneMatch(s -> isTimeOverlapping(slotStart, slotEnd, s.getStartTime(), s.getEndTime()));
            
            if (isAvailable) {
                availability.add(new TimeSlot(slotStart, slotEnd));
            }
            
            current = current.plusHours(1);
        }
        
        return availability;
    }

    /**
     * Get schedule for a specific date range
     */
    public List<Schedule> getScheduleForDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByStartTimeBetweenOrderByStartTime(startDate, endDate);
    }

    /**
     * Get instructor schedule
     */
    public List<Schedule> getInstructorSchedule(Long instructorId, LocalDateTime startDate, LocalDateTime endDate) {
        Instructor instructor = instructorRepository.findById(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));
        
        return scheduleRepository.findByInstructorAndTimeRange(instructor, startDate, endDate);
    }

    /**
     * Get student schedule
     */
    public List<Schedule> getStudentSchedule(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return scheduleRepository.findByStudentAndTimeRange(student, startDate, endDate);
    }

    /**
     * Cancel a schedule entry
     */
    public Schedule cancelSchedule(Long scheduleId, String reason) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        schedule.setStatus(Schedule.ScheduleStatus.CANCELLED);
        schedule.setCancelledAt(LocalDateTime.now());
        schedule.setCancellationReason(reason);
        
        return scheduleRepository.save(schedule);
    }

    /**
     * Reschedule an entry
     */
    public Schedule reschedule(Long scheduleId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        LocalDateTime oldStartTime = schedule.getStartTime();
        LocalDateTime oldEndTime = schedule.getEndTime();
        
        schedule.setRescheduledFrom(oldStartTime);
        schedule.setStartTime(newStartTime);
        schedule.setEndTime(newEndTime);
        schedule.setStatus(Schedule.ScheduleStatus.RESCHEDULED);
        
        // Check for new conflicts
        List<ScheduleConflict> conflicts = detectConflicts(schedule);
        if (!conflicts.isEmpty()) {
            scheduleConflictRepository.saveAll(conflicts);
        }
        
        return scheduleRepository.save(schedule);
    }

    // Helper methods
    private boolean isTimeOverlapping(LocalDateTime start1, LocalDateTime end1, 
                                    LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private LocalDateTime calculateNextOccurrence(LocalDateTime current, RecurrencePattern pattern) {
        switch (pattern.getFrequency()) {
            case DAILY:
                return current.plusDays(pattern.getInterval());
            case WEEKLY:
                return current.plusWeeks(pattern.getInterval());
            case MONTHLY:
                return current.plusMonths(pattern.getInterval());
            default:
                return current.plusDays(1);
        }
    }

    /**
     * Inner class for time slots
     */
    public static class TimeSlot {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        
        public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        // Getters and setters
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    }

    /**
     * Inner class for recurrence patterns
     */
    public static class RecurrencePattern {
        private Frequency frequency;
        private int interval;
        private int occurrences;
        private List<DayOfWeek> daysOfWeek;
        
        public RecurrencePattern(Frequency frequency, int interval, int occurrences) {
            this.frequency = frequency;
            this.interval = interval;
            this.occurrences = occurrences;
        }
        
        public String toJson() {
            // Simple JSON representation
            return String.format("{\"frequency\":\"%s\",\"interval\":%d,\"occurrences\":%d}", 
                frequency.toString(), interval, occurrences);
        }
        
        // Getters and setters
        public Frequency getFrequency() { return frequency; }
        public void setFrequency(Frequency frequency) { this.frequency = frequency; }
        public int getInterval() { return interval; }
        public void setInterval(int interval) { this.interval = interval; }
        public int getOccurrences() { return occurrences; }
        public void setOccurrences(int occurrences) { this.occurrences = occurrences; }
        public List<DayOfWeek> getDaysOfWeek() { return daysOfWeek; }
        public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) { this.daysOfWeek = daysOfWeek; }
        
        public enum Frequency {
            DAILY, WEEKLY, MONTHLY, YEARLY
        }
    }
}
