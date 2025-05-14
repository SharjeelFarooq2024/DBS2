package com.TutorManagementSystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_request")  // Changed from tutoring_request to student_request
public class StudentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "tutor_id")
    private Long tutorId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public StudentRequest() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Long getTutorId() { 
        return tutorId; 
    }
    
    public void setTutorId(Long tutorId) { 
        this.tutorId = tutorId; 
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Subject getSubject() { 
        return subject; 
    }
    
    public void setSubject(Subject subject) { 
        this.subject = subject; 
    }
}