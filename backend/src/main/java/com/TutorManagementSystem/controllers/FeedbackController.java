package com.TutorManagementSystem.controllers;

import com.TutorManagementSystem.dto.FeedbackDTO;
import com.TutorManagementSystem.model.Feedback;
import com.TutorManagementSystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.parseLong(request.get("studentId").toString());
            Long tutorId = Long.parseLong(request.get("tutorId").toString());
            Integer rating = Integer.parseInt(request.get("rating").toString());
            String comment = (String) request.get("comment");

            Feedback feedback = feedbackService.createFeedback(studentId, tutorId, rating, comment);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<?> getTutorFeedback(@PathVariable Long tutorId) {
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getTutorFeedbackDTOs(tutorId);
            Map<String, Object> summary = feedbackService.getTutorFeedbackSummary(tutorId);

            Map<String, Object> response = new HashMap<>();
            response.put("feedbacks", feedbacks);
            response.put("summary", summary);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}