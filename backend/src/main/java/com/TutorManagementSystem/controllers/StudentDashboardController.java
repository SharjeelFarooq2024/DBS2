package com.TutorManagementSystem.controllers;

import com.TutorManagementSystem.model.StudentRequest;
import com.TutorManagementSystem.model.Student;
import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.model.Tutor;
import com.TutorManagementSystem.service.StudentRequestService;
import com.TutorManagementSystem.service.JobService;
import com.TutorManagementSystem.repository.SubjectRepository;
import com.TutorManagementSystem.repository.TutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentDashboardController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRequestService studentRequestService;

    @Autowired
    private JobService jobService;

    @Autowired
    private TutorRepository tutorRepository;

    @GetMapping("/current-subjects/{studentId}")
    public ResponseEntity<List<Subject>> getCurrentSubjects(@PathVariable Long studentId) {
        try {
            // Use proper method name matching the Subject entity
            List<Subject> subjects = subjectRepository.findByStudentId(studentId);
            
            if (subjects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/subjects/{ids}")
    public ResponseEntity<List<Subject>> getSubjectsByIds(@PathVariable List<Long> ids) {
        try {
            // Use proper method name matching the Subject entity
            List<Subject> subjects = subjectRepository.findBySubjectIdIn(ids);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        try {
            List<Subject> subjects = subjectRepository.findAll();
            if (subjects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{studentId}/requests")
    public ResponseEntity<List<Map<String, Object>>> getStudentRequests(@PathVariable Long studentId) {
        List<StudentRequest> requests = studentRequestService.getRequestsByStudentId(studentId);
        
        List<Map<String, Object>> formattedRequests = requests.stream().map(request -> {
            Map<String, Object> formatted = new HashMap<>();
            formatted.put("requestId", request.getRequestId());
            formatted.put("subject", request.getSubject().getName());
            formatted.put("description", request.getDescription());
            formatted.put("status", request.getStatus());
            formatted.put("createdAt", request.getCreatedAt());

            // If request is accepted, get tutor name from job
            if ("ACCEPTED".equals(request.getStatus())) {
                jobService.findByRequestId(request.getRequestId()).ifPresent(job -> {
                    formatted.put("tutorName", jobService.getTutorName(job.getTutorId()));
                });
            }

            return formatted;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(formattedRequests);
    }

    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(@RequestBody StudentRequest request) {
        try {
            StudentRequest savedRequest = studentRequestService.createRequest(request);
            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/requests/{requestId}")  // Changed from /student/requests/{requestId}
    public ResponseEntity<?> cancelRequest(@PathVariable Long requestId) {
        try {
            studentRequestService.cancelRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{studentId}/completed-tutors")
    public ResponseEntity<?> getCompletedTutors(@PathVariable Long studentId) {
        try {
            // Get all completed and accepted requests for this student
            List<StudentRequest> completedRequests = studentRequestService
                .getRequestsByStudentIdAndStatus(studentId, "COMPLETED");
            List<StudentRequest> acceptedRequests = studentRequestService
                .getRequestsByStudentIdAndStatus(studentId, "ACCEPTED");
            
            // Combine both lists
            completedRequests.addAll(acceptedRequests);
            
            // Get unique tutor IDs
            Set<Long> tutorIds = completedRequests.stream()
                .map(StudentRequest::getTutorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
                
            // Get tutor details
            List<Map<String, Object>> tutors = tutorIds.stream()
                .map(tutorId -> {
                    Tutor tutor = tutorRepository.findById(tutorId)
                        .orElse(null);
                    if (tutor != null) {
                        Map<String, Object> tutorMap = new HashMap<>();
                        tutorMap.put("id", tutor.getId());
                        tutorMap.put("name", tutor.getName());
                        return tutorMap;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(tutors);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error getting tutors: " + e.getMessage()));
        }
    }

    @GetMapping("/{studentId}/accepted-tutors")
    public ResponseEntity<?> getAcceptedTutors(@PathVariable Long studentId) {
        try {
            // Get all accepted requests for this student
            List<StudentRequest> acceptedRequests = studentRequestService
                .getRequestsByStudentIdAndStatus(studentId, "ACCEPTED");
            
            if (acceptedRequests.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Convert to tutor response objects
            List<Map<String, Object>> tutorResponses = new ArrayList<>();
            
            for (StudentRequest request : acceptedRequests) {
                Tutor tutor = tutorRepository.findById(request.getTutorId()).orElse(null);
                if (tutor != null) {
                    Map<String, Object> tutorResponse = new HashMap<>();
                    tutorResponse.put("id", tutor.getId());
                    tutorResponse.put("name", tutor.getName());
                    tutorResponse.put("requestId", request.getRequestId());
                    tutorResponses.add(tutorResponse);
                }
            }
            
            return ResponseEntity.ok(tutorResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error getting tutors: " + e.getMessage()));
        }
    }
}

class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
