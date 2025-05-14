package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTutorId(Long tutorId);
    List<Feedback> findByStudentId(Long studentId);
    boolean existsByStudentIdAndTutorId(Long studentId, Long tutorId);
}