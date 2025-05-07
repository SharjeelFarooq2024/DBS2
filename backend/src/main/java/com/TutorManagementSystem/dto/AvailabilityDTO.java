package com.TutorManagementSystem.dto;

public class AvailabilityDTO {
    private Long availabilityId;
    private Long tutorId;
    private Long subjectId;
    private String subjectName;
    private String dayOfWeek;
    private String timeSlot;

    public AvailabilityDTO() {}

    public AvailabilityDTO(Long availabilityId, Long tutorId, Long subjectId, String subjectName, String dayOfWeek, String timeSlot) {
        this.availabilityId = availabilityId;
        this.tutorId = tutorId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
