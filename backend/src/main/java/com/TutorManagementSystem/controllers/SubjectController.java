package com.TutorManagementSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.model.Tutor;
import com.TutorManagementSystem.repository.SubjectRepository;
import com.TutorManagementSystem.repository.TutorRepository;
import com.TutorManagementSystem.service.TutorService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private TutorService tutorService;

    // GET /subjects → Return list of available subjects
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        try {
            List<Subject> subjects = subjectRepository.findAll();
            // Always return OK with the list, even if empty
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            e.printStackTrace();
            // Return an empty list instead of an error to ensure valid JSON
            return ResponseEntity.ok(List.of());
        }
    }

    // GET /tutors?subject=math → Return tutors for that subject
    @GetMapping("/tutors")
    public ResponseEntity<List<Tutor>> getTutorsBySubject(@RequestParam String subject) {
        try {
            Subject subj = subjectRepository.findByNameIgnoreCase(subject)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + subject));
            return ResponseEntity.ok(tutorRepository.findBySubjectsId(subj.getSubjectId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET /tutor/{tutor_id} → Return full tutor profile + availability
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<Map<String, Object>> getTutorProfile(@PathVariable Long tutorId) {
        try {
            Map<String, Object> profile = new HashMap<>();
            
            Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));
                
            profile.put("tutor", tutor);
            profile.put("subjects", tutorService.getSubjectsForTutor(tutorId));
            profile.put("availability", tutorService.getAvailabilityForTutor(tutorId));
            
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}