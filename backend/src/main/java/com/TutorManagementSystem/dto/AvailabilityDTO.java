package com.TutorManagementSystem.dto;

public class AvailabilityDTO {
    private Long id;
    private Long tutorId;
    private Long subjectId;
    private String dayOfWeek;
    private String timeSlot;

    public AvailabilityDTO() {}

    public AvailabilityDTO(Long id, Long tutorId, Long subjectId, String dayOfWeek, String timeSlot) {
        this.id = id;
        this.tutorId = tutorId;
        this.subjectId = subjectId;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
