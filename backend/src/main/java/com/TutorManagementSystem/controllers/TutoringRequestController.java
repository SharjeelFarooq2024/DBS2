package com.TutorManagementSystem.controllers;

import com.TutorManagementSystem.model.StudentRequest;
import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.model.Student;
import com.TutorManagementSystem.service.TutoringRequestService;
import com.TutorManagementSystem.repository.SubjectRepository;
import com.TutorManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TutoringRequestController {
    
    @Autowired
    private TutoringRequestService requestService;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @PostMapping("/student-request")
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> requestData) {
        try {
            // Input validation
            if (requestData.get("studentId") == null || requestData.get("subjectId") == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Student ID and Subject ID are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            StudentRequest request = new StudentRequest();
            
            // Get and validate student
            Long studentId;
            try {
                studentId = Long.parseLong(requestData.get("studentId").toString());
            } catch (NumberFormatException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid student ID format");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            request.setStudent(student);
            
            // Get and validate subject
            Long subjectId;
            try {
                subjectId = Long.parseLong(requestData.get("subjectId").toString());
            } catch (NumberFormatException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid subject ID format");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
            request.setSubject(subject);
            
            // Set description
            String description = requestData.get("description") != null ? 
                requestData.get("description").toString() : "";
            request.setDescription(description);
            
            StudentRequest savedRequest = requestService.saveRequest(request);
            
            // Return a properly structured response
            Map<String, Object> response = new HashMap<>();
            response.put("requestId", savedRequest.getRequestId());
            response.put("status", savedRequest.getStatus());
            response.put("message", "Request created successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create request: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/student-request/status/{id}")
    public ResponseEntity<?> getStatus(@PathVariable Long id) {
        try {
            String status = requestService.getStatusById(id);
            
            // Make sure we're always returning a valid JSON object
            if (status == null) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "UNKNOWN");
                return ResponseEntity.ok(response);
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get request status: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}