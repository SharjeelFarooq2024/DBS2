package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
     List<Feedback> findByTutorId(Long tutorId);
    List<Feedback> findByStudentId(Long studentId);

    boolean existsByStudentIdAndTutorId(Long studentId, Long tutorId);

   @Query("SELECT f.tutor.id FROM Feedback f WHERE f.student.id = :studentId")
   List<Long> findTutorIdsByStudentId(@Param("studentId") Long studentId);
}
