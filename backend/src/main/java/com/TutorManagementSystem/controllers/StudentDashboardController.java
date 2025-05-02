package com.TutorManagementSystem.controllers;

import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.repository.SubjectRepository;
import com.TutorManagementSystem.service.StudentRequestService;
import com.TutorManagementSystem.service.JobService;
import com.TutorManagementSystem.model.StudentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
}
