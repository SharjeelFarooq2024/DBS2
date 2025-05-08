package com.TutorManagementSystem.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "tutor")
@PrimaryKeyJoinColumn(name = "id") // Specifies the join column in the tutor table
public class Tutor extends User {
    
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TutorSubject> tutorSubjects;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities;

    private String specialization;
    private String qualifications;

    // Getters and setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    
    public List<TutorSubject> getTutorSubjects() { return tutorSubjects; }
    public void setTutorSubjects(List<TutorSubject> tutorSubjects) { this.tutorSubjects = tutorSubjects; }

    public List<Availability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(List<Availability> availabilities) { this.availabilities = availabilities; }
}