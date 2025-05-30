package com.TutorManagementSystem.controllers;

import com.TutorManagementSystem.model.StudentRequest;
import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.model.Job;
import com.TutorManagementSystem.service.StudentRequestService;
import com.TutorManagementSystem.service.TutorSubjectService;
import com.TutorManagementSystem.service.TutorService;
import com.TutorManagementSystem.service.JobService;
import com.TutorManagementSystem.repository.TutorRepository;
import com.TutorManagementSystem.repository.AvailabilityRepository;
import com.TutorManagementSystem.model.Tutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
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

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @GetMapping("/tutor-info/{tutorId}")  // Changed from "/tutor/{tutorId}"
    public ResponseEntity<?> getTutorById(@PathVariable Long tutorId) {
        try {
            Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));
                
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", tutor.getId());
            profile.put("name", tutor.getName());
            profile.put("email", tutor.getEmail());
            profile.put("specialization", tutor.getSpecialization());
            profile.put("qualifications", tutor.getQualifications());
            
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/tutor/{tutorId}/profile")
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

    @PostMapping("/tutor/add-availability")
    public ResponseEntity<?> addAvailability(@RequestBody Map<String, Object> body) {
        try {
            // Debug logging
            System.out.println("Request body: " + body);
            
            // Extract parameters correctly
            Long tutorId = Long.valueOf(body.get("tutorId").toString());
            Long subjectId = Long.valueOf(body.get("subjectId").toString());
            String day = (String) body.get("day");
            // Accept either "time" or "timeSlot"
            String time = body.get("time") != null ? (String) body.get("time") : (String) body.get("timeSlot");
            
            // Validate inputs
            if (day == null || time == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Day and time are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Call service
            tutorService.addAvailability(tutorId, subjectId, day, time);
            
            // Return success
            Map<String, String> response = new HashMap<>();
            response.put("message", "Availability added successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/tutor/add-subject")
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

    @GetMapping("/tutor/{tutorId}/requests")
    public ResponseEntity<?> getStudentRequests(@PathVariable Long tutorId) {
        try {
            // Return all requests for this tutor's subjects, not just pending
            List<StudentRequest> requests = studentRequestService.getAllRequestsForTutor(tutorId);
            List<Map<String, Object>> formattedRequests = new ArrayList<>();
            if (requests != null) {
                formattedRequests = requests.stream().map(request -> {
                    Map<String, Object> formatted = new HashMap<>();
                    formatted.put("requestId", request.getRequestId());
                    formatted.put("studentName", studentRequestService.getStudentName(request.getStudent().getId()));
                    formatted.put("subjectName", request.getSubject().getName());
                    formatted.put("description", request.getDescription());
                    formatted.put("status", request.getStatus());
                    formatted.put("createdAt", request.getCreatedAt());
                    return formatted;
                }).collect(Collectors.toList());
            }
            return ResponseEntity.ok(formattedRequests);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch student requests: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/tutor/requests/{requestId}")
    public ResponseEntity<Void> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");
            Long tutorId = Long.parseLong(body.get("tutorId"));
            
            if ("ACCEPTED".equals(status)) {
                studentRequestService.acceptRequest(requestId, tutorId);
                // ... assign job, etc ...
            } else {
                studentRequestService.updateRequestStatus(requestId, status);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tutor/{tutorId}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsByTutorId(@PathVariable Long tutorId) {
        return ResponseEntity.ok(tutorSubjectService.getSubjectsByTutorId(tutorId));
    }

    @DeleteMapping("/tutor/availability/{availabilityId}")
    public ResponseEntity<?> deleteAvailability(@PathVariable Long availabilityId) {
        try {
            if (!availabilityRepository.existsById(availabilityId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Not Found"));
            }
            availabilityRepository.deleteById(availabilityId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/tutors/subject/{subjectId}")
    public ResponseEntity<?> getTutorsBySubject(@PathVariable Long subjectId) {
        try {
            List<Tutor> tutors = tutorRepository.findBySubjectsId(subjectId);
            List<Map<String, Object>> tutorList = tutors.stream()
                .map(tutor -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tutor.getId());
                    map.put("name", tutor.getName());
                    map.put("qualifications", tutor.getQualifications());
                    map.put("specialization", tutor.getSpecialization());
                    return map;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(tutorList);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Collections.singletonMap("error", "Error finding tutors: " + e.getMessage()));
        }
    }
}
