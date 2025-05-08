package com.TutorManagementSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;
    
    private String grade;            // For students
    private String specialization;   // For tutors
    private String qualifications;   // For tutors

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public String getQualifications() {
        return qualifications;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
}