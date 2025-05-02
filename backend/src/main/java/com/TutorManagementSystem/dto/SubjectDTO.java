package com.TutorManagementSystem.dto;

public class SubjectDTO {
    private Long subject_id;
    private String subjectName;
    private String description;



    // Constructors
    public SubjectDTO() {}
    public SubjectDTO(Long subject_id, String subjectName, String description) {
        this.subject_id = subject_id;
        this.subjectName = subjectName;
        this.description = description;
    }

    // Getters and setters
    public Long getSubject_id() {
        return subject_id;
    }
    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
