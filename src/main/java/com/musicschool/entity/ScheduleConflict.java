package com.musicschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entity representing a schedule conflict.
 */
@Entity
@Table(name = "schedule_conflicts")
public class ScheduleConflict extends BaseEntity {

    @NotNull(message = "Schedule is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @NotNull(message = "Conflicting schedule is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conflicting_schedule_id", nullable = false)
    private Schedule conflictingSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "conflict_type")
    private ConflictType conflictType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "resolved")
    private Boolean resolved = false;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes", length = 1000)
    private String resolutionNotes;

    // Constructors
    public ScheduleConflict() {}

    public ScheduleConflict(Schedule schedule, Schedule conflictingSchedule, ConflictType conflictType) {
        this.schedule = schedule;
        this.conflictingSchedule = conflictingSchedule;
        this.conflictType = conflictType;
    }

    // Getters and Setters
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getConflictingSchedule() {
        return conflictingSchedule;
    }

    public void setConflictingSchedule(Schedule conflictingSchedule) {
        this.conflictingSchedule = conflictingSchedule;
    }

    public ConflictType getConflictType() {
        return conflictType;
    }

    public void setConflictType(ConflictType conflictType) {
        this.conflictType = conflictType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public enum ConflictType {
        INSTRUCTOR_DOUBLE_BOOKING,
        ROOM_DOUBLE_BOOKING,
        STUDENT_DOUBLE_BOOKING,
        TIME_OVERLAP,
        RESOURCE_CONFLICT
    }
}
