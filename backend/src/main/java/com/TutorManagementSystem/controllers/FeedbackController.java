package com.TutorManagementSystem.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.TutorManagementSystem.model.RatingFeedback;
import com.TutorManagementSystem.service.FeedBackService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedBackService feedbackService;

    // POST /feedback → Submit feedback after job is done
    @PostMapping("/feedback")
    public ResponseEntity<RatingFeedback> submitFeedback(@RequestBody RatingFeedback feedback) {
        return ResponseEntity.ok(feedbackService.submitFeedback(feedback));
    }

    // GET /feedback/tutor/{tutorId} → Return feedback with tutor profile (optional)
    @GetMapping("/feedback/tutor/{tutorId}")
    public ResponseEntity<List<RatingFeedback>> ggetFeedbackByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByJobId(jobId));
    }
}
