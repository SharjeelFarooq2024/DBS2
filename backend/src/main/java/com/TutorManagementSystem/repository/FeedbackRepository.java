package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.RatingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<RatingFeedback, Long> {
    List<RatingFeedback> findByJobJobId(Long jobId);


}
