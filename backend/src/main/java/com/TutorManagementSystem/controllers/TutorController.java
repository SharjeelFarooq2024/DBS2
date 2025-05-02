package com.TutorManagementSystem.controllers;

import com.TutorManagementSystem.model.StudentRequest;
import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.model.Job;
import com.TutorManagementSystem.service.StudentRequestService;
import com.TutorManagementSystem.service.TutorSubjectService;
import com.TutorManagementSystem.service.TutorService;
import com.TutorManagementSystem.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutor")
@CrossOrigin(origins = "*")
public class TutorController {

    @Autowired
    private StudentRequestService studentRequestService;
    
    @Autowired
    private TutorSubjectService tutorSubjectService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private JobService jobService;

    @GetMapping("/{tutorId}/profile")
    public ResponseEntity<?> getTutorProfile(@PathVariable Long tutorId) {
        try {
            Map<String, Object> profile = new HashMap<>();
            profile.put("subjects", tutorService.getSubjectsForTutor(tutorId));
            profile.put("availability", tutorService.getAvailabilityForTutor(tutorId));
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch tutor profile: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/add-availability")
    public ResponseEntity<?> addAvailability(@RequestBody Map<String, String> body) {
        try {
            Long tutorId = Long.parseLong(body.get("tutorId"));
            Long subjectId = Long.parseLong(body.get("subjectId"));
            String day = body.get("day");
            String time = body.get("time");
            
            if (day == null || time == null || subjectId == null) {
                throw new RuntimeException("Day, time, and subject are required");
            }
            
            tutorService.addAvailability(tutorId, subjectId, day, time);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Availability added successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "An unexpected error occurred");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/add-subject")
    public ResponseEntity<?> addSubject(@RequestBody Map<String, String> body) {
        try {
            Long tutorId = Long.parseLong(body.get("tutorId"));
            Long subjectId = Long.parseLong(body.get("subjectId"));
            
            tutorService.addSubject(tutorId, subjectId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Subject added successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "An unexpected error occurred");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{tutorId}/requests")
    public ResponseEntity<?> getStudentRequests(@PathVariable Long tutorId) {
        try {
            List<StudentRequest> requests = studentRequestService.getPendingRequestsForTutor(tutorId);
            
            List<Map<String, Object>> formattedRequests = new ArrayList<>();
            if (requests != null) {
                formattedRequests = requests.stream()
                    .map(req -> {
                        Map<String, Object> formatted = new HashMap<>();
                        formatted.put("requestId", req.getRequestId());
                        formatted.put("studentName", req.getStudent() != null ? req.getStudent().getName() : "Unknown");
                        formatted.put("subject", req.getSubject() != null ? req.getSubject().getName() : "Not specified");
                        formatted.put("description", req.getDescription() != null ? req.getDescription() : "");
                        formatted.put("createdAt", req.getCreatedAt() != null ? req.getCreatedAt().toString() : "");
                        formatted.put("status", req.getStatus() != null ? req.getStatus() : "PENDING");
                        return formatted;
                    })
                    .collect(Collectors.toList());
            }
            
            return ResponseEntity.ok(formattedRequests);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch student requests: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/requests/{requestId}")
    public ResponseEntity<Void> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");
            Long tutorId = Long.parseLong(body.get("tutorId"));
            
            StudentRequest request = studentRequestService.updateRequestStatus(requestId, status);
            
            if (status.equals("ACCEPTED")) {
                Job job = new Job();
                job.setRequest(request);
                job.setTutorId(tutorId);
                job.setStudentId(request.getStudent().getId());
                job.setStatus("ASSIGNED");
                jobService.assignJob(job);
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{tutorId}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsByTutorId(@PathVariable Long tutorId) {
        return ResponseEntity.ok(tutorSubjectService.getSubjectsByTutorId(tutorId));
    }
}
