package com.TutorManagementSystem.service;

import com.TutorManagementSystem.dto.FeedbackDTO;
import com.TutorManagementSystem.model.Feedback;
import com.TutorManagementSystem.model.Student;
import com.TutorManagementSystem.model.Tutor;
import com.TutorManagementSystem.repository.FeedbackRepository;
import com.TutorManagementSystem.repository.StudentRepository;
import com.TutorManagementSystem.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TutorRepository tutorRepository;

    public Feedback createFeedback(Long studentId, Long tutorId, Integer rating, String comment) {
        if (feedbackRepository.existsByStudentIdAndTutorId(studentId, tutorId)) {
            throw new RuntimeException("You have already provided feedback for this tutor");
        }

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
            
        Tutor tutor = tutorRepository.findById(tutorId)
            .orElseThrow(() -> new RuntimeException("Tutor not found"));

        Feedback feedback = new Feedback();
        feedback.setStudent(student);
        feedback.setTutor(tutor);
        feedback.setRating(rating);
        feedback.setComment(comment);

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getTutorFeedback(Long tutorId) {
        return feedbackRepository.findByTutorId(tutorId);
    }

    public Map<String, Object> getTutorFeedbackSummary(Long tutorId) {
        List<Feedback> feedbacks = feedbackRepository.findByTutorId(tutorId);
        
        Map<String, Object> summary = new HashMap<>();
        if (feedbacks.isEmpty()) {
            summary.put("averageRating", 0.0);
            summary.put("totalFeedbacks", 0);
            return summary;
        }

        double averageRating = feedbacks.stream()
            .mapToInt(Feedback::getRating)
            .average()
            .orElse(0.0);

        summary.put("averageRating", Math.round(averageRating * 10.0) / 10.0);
        summary.put("totalFeedbacks", feedbacks.size());
        return summary;
    }

    public List<Long> getTutorsWithFeedbackFromStudent(Long studentId) {
        return feedbackRepository.findTutorIdsByStudentId(studentId);
    }

    public List<FeedbackDTO> getTutorFeedbackDTOs(Long tutorId) {
        List<Feedback> feedbacks = feedbackRepository.findByTutorId(tutorId);
        return feedbacks.stream().map(fb -> {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setId(fb.getId());
            dto.setComment(fb.getComment());
            dto.setCreatedAt(fb.getCreatedAt());
            dto.setRating(fb.getRating());
            dto.setStudentId(fb.getStudent().getId());
            // Fetch student name
            Student student = fb.getStudent();
            dto.setStudentName(student != null ? student.getName() : "Anonymous");
            return dto;
        }).collect(Collectors.toList());
    }
}