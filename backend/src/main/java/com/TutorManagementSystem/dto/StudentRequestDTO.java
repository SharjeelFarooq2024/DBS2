package com.TutorManagementSystem.dto;

public class StudentRequestDTO {
    private Long requestId;
    private String studentName;
    private String subjectName;
    private String status;

    public StudentRequestDTO() {}

    public StudentRequestDTO(Long requestId, String studentName, String subjectName, String status) {
        this.requestId = requestId;
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
