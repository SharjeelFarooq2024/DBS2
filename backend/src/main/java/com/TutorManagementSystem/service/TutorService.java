package com.TutorManagementSystem.service;

import com.TutorManagementSystem.dto.AvailabilityDTO;
import com.TutorManagementSystem.dto.SubjectDTO;
import com.TutorManagementSystem.dto.StudentRequestDTO;
import com.TutorManagementSystem.model.*;
import com.TutorManagementSystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TutorService {

    private final SubjectRepository subjectRepository;
    private final AvailabilityRepository availabilityRepository;
    private final StudentRequestRepository studentRequestRepository;
    private final TutorRepository tutorRepository;

    public TutorService(
            SubjectRepository subjectRepository,
            AvailabilityRepository availabilityRepository,
            StudentRequestRepository studentRequestRepository,
            TutorRepository tutorRepository) {
        this.subjectRepository = subjectRepository;
        this.availabilityRepository = availabilityRepository;
        this.studentRequestRepository = studentRequestRepository;
        this.tutorRepository = tutorRepository;
    }

    // Add availability for a tutor
    @Transactional
    public void addAvailability(Long tutorId, Long subjectId, String day, String timeSlot) {
        try {
            // Debug logging
            System.out.println("Adding availability: " + tutorId + ", " + subjectId + ", " + day + ", " + timeSlot);
            
            // Get tutor entity
            Tutor tutor = tutorRepository.findById(tutorId)
                    .orElseThrow(() -> new RuntimeException("Tutor not found"));
            
            // Get subject entity
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            // Check if tutor teaches this subject
            boolean teachesSubject = tutor.getTutorSubjects().stream()
                    .anyMatch(ts -> ts.getSubject().getSubjectId().equals(subjectId));
                    
            if (!teachesSubject) {
                throw new RuntimeException("Tutor does not teach this subject");
            }

            // Check if slot already exists (use the new repository method)
            boolean slotExists = availabilityRepository.existsByTutorAndSubjectAndDayAndTime(
                    tutorId, subjectId, day, timeSlot);
            
            if (slotExists) {
                throw new RuntimeException("This time slot already exists for this subject");
            }

            // Create and save the new availability
            Availability availability = new Availability();
            availability.setTutor(tutor);
            availability.setSubject(subject);
            availability.setDayOfWeek(day);
            availability.setTimeSlot(timeSlot);
            
            // Save the availability - THIS IS CRITICAL!
            Availability savedAvailability = availabilityRepository.save(availability);
            System.out.println("Saved availability with ID: " + savedAvailability.getId());
            
        } catch (RuntimeException e) {
            System.err.println("Error adding availability: " + e.getMessage());
            throw e; // Rethrow to be handled by controller
        } catch (Exception e) {
            System.err.println("Unexpected error adding availability: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add availability: " + e.getMessage());
        }
    }

    // Add subject for a tutor
    @Transactional
    public void addSubject(Long tutorId, Long subjectId) {
        try {
            Tutor tutor = tutorRepository.findByIdWithSubjects(tutorId)
                    .orElseThrow(() -> new RuntimeException("Tutor not found"));
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            if (tutor.getTutorSubjects() == null) {
                tutor.setTutorSubjects(new ArrayList<>());
            }

            // Check if the relationship already exists
            boolean alreadyExists = tutor.getTutorSubjects().stream()
                    .anyMatch(ts -> ts.getSubject().getSubjectId().equals(subjectId));
            
            if (alreadyExists) {
                // Instead of throwing an error, we'll just return successfully since the subject is already added
                return;
            }

            TutorSubject tutorSubject = new TutorSubject();
            tutorSubject.setTutor(tutor);
            tutorSubject.setSubject(subject);
            
            tutor.getTutorSubjects().add(tutorSubject);
            tutorRepository.save(tutor);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Failed to add subject: " + e.getMessage());
        }
    }

    // Get all subjects for a tutor
    public List<SubjectDTO> getSubjectsForTutor(Long tutorId) {
        return subjectRepository.findSubjectsByTutorId(tutorId)
                .stream()
                .map(subject -> new SubjectDTO(
                    subject.getSubjectId(),
                    subject.getName(),
                    subject.getDescription()
                ))
                .collect(Collectors.toList());
    }

    // Get all availability slots for a tutor
    public List<AvailabilityDTO> getAvailabilityForTutor(Long tutorId) {
        return availabilityRepository.findByTutorId(tutorId)
                .stream()
                .map(availability -> {
                    AvailabilityDTO dto = new AvailabilityDTO();
                    dto.setAvailabilityId(availability.getId());  // Changed from setId to setAvailabilityId
                    dto.setTutorId(availability.getTutor().getId());
                    dto.setSubjectId(availability.getSubject().getSubjectId());
                    dto.setSubjectName(availability.getSubject().getName());  // Added subjectName
                    dto.setDayOfWeek(availability.getDayOfWeek());
                    dto.setTimeSlot(availability.getTimeSlot());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Check if tutor is available for teaching
    public boolean isTutorAvailable(Long tutorId) {
        Tutor tutor = tutorRepository.findByIdWithSubjects(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));
        List<AvailabilityDTO> availability = getAvailabilityForTutor(tutorId);
        return tutor.getTutorSubjects() != null && 
               !tutor.getTutorSubjects().isEmpty() && 
               !availability.isEmpty();
    }

    // Get all student requests for a tutor
    public List<StudentRequestDTO> getRequestsForTutor(Long tutorId) {
        List<StudentRequest> requests = studentRequestRepository.findPendingRequests();
        return requests.stream()
                .map(request -> {
                    Student student = request.getStudent();
                    Subject subject = request.getSubject();
                    return new StudentRequestDTO(
                            request.getRequestId(),
                            student.getName(),
                            subject.getName(),
                            request.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    // Find tutors by subject
    public List<Tutor> findTutorsBySubject(Subject subject) {
        return tutorRepository.findBySubjectsId(subject.getSubjectId());
    }

    // Get subjects taught by a tutor
    public List<Subject> getTutorSubjects(Long tutorId) {
        return tutorRepository.findByIdWithSubjects(tutorId)
                .map(tutor -> tutor.getTutorSubjects().stream()
                        .map(TutorSubject::getSubject)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Tutor not found"));
    }

    // Method to get tutor profile with subjects and availability
    public Map<String, Object> getTutorProfile(Long tutorId) {
        try {
            // Get the tutor and their subjects
            Tutor tutor = tutorRepository.findById(tutorId)
                    .orElseThrow(() -> new RuntimeException("Tutor not found"));

            Map<String, Object> profile = new HashMap<>();
            
            // Get subjects taught by this tutor - Fix the method call here
            List<Map<String, Object>> subjects = subjectRepository.findSubjectsByTutorId(tutorId)
                    .stream()
                    .map(subject -> {
                        Map<String, Object> subjectMap = new HashMap<>();
                        subjectMap.put("subject_id", subject.getSubjectId());
                        subjectMap.put("subjectName", subject.getName());
                        return subjectMap;
                    })
                    .collect(Collectors.toList());
            
            // Get availability slots for this tutor
            List<Map<String, Object>> availability = availabilityRepository.findByTutorId(tutorId)
                    .stream()
                    .map(slot -> {
                        Map<String, Object> availabilityMap = new HashMap<>();
                        availabilityMap.put("availabilityId", slot.getId());
                        availabilityMap.put("subjectId", slot.getSubject().getSubjectId());
                        availabilityMap.put("subjectName", slot.getSubject().getName());
                        availabilityMap.put("dayOfWeek", slot.getDayOfWeek());
                        availabilityMap.put("timeSlot", slot.getTimeSlot());
                        return availabilityMap;
                    })
                    .collect(Collectors.toList());
            
            profile.put("subjects", subjects);
            profile.put("availability", availability);
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get tutor profile: " + e.getMessage());
        }
    }
}
