package com.TutorManagementSystem.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends User {
    @Column(name = "grade")
    private String grade;

    @OneToMany(mappedBy = "student")
    private List<StudentSubject> studentSubjects;

    // Getters and setters
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public List<StudentSubject> getStudentSubjects() { return studentSubjects; }
    public void setStudentSubjects(List<StudentSubject> studentSubjects) { this.studentSubjects = studentSubjects; }
}
