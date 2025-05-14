package com.TutorManagementSystem.service;

import com.TutorManagementSystem.model.StudentRequest;
import com.TutorManagementSystem.model.User;
import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.repository.StudentRequestRepository;
import com.TutorManagementSystem.repository.UserRepository;
import com.TutorManagementSystem.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class StudentRequestService {

    @Autowired
    private StudentRequestRepository studentRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Get all student requests
    public List<StudentRequest> getAllRequests() {
        return studentRequestRepository.findAll();
    }

    // Get a single request by ID
    public Optional<StudentRequest> getRequestById(Long id) {
        return studentRequestRepository.findById(id);
    }

    // Create a new student request
    public StudentRequest createRequest(StudentRequest request) {
        // Set initial status if not set
        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }
        return studentRequestRepository.save(request);
    }

    // Update an existing request
    public StudentRequest updateRequest(Long id, StudentRequest updatedRequest) {
        Optional<StudentRequest> existingRequestOpt = studentRequestRepository.findById(id);

        if (existingRequestOpt.isPresent()) {
            StudentRequest existingRequest = existingRequestOpt.get();
            existingRequest.setStudent(updatedRequest.getStudent());
            existingRequest.setSubject(updatedRequest.getSubject());
            existingRequest.setDescription(updatedRequest.getDescription());
            existingRequest.setStatus(updatedRequest.getStatus());
            existingRequest.setCreatedAt(updatedRequest.getCreatedAt());

            return studentRequestRepository.save(existingRequest);
        } else {
            throw new RuntimeException("Request with ID " + id + " not found.");
        }
    }

    // Delete a request
    public void deleteRequest(Long id) {
        studentRequestRepository.deleteById(id);
    }

    // Get assigned requests for a student
    public List<StudentRequest> getAssignedRequestsByStudentId(Long studentId) {
        return studentRequestRepository.findByStudentIdAndStatus(studentId, "ASSIGNED");
    }

    // Get pending requests for a tutor
    public List<StudentRequest> getPendingRequestsForTutor(Long tutorId) {
        try {
            return studentRequestRepository.findByStatusAndTutorIdOrderByCreatedAtDesc("PENDING", tutorId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Update request status
    public StudentRequest updateRequestStatus(Long requestId, String status) {
        StudentRequest request = studentRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return studentRequestRepository.save(request);
    }

    // Get all requests for a student
    public List<StudentRequest> getRequestsByStudentId(Long studentId) {
        return studentRequestRepository.findByStudentId(studentId);
    }

    // Get requests by student ID and status
    public List<StudentRequest> getRequestsByStudentIdAndStatus(Long studentId, String status) {
        return studentRequestRepository.findByStudentIdAndStatusWithTutor(studentId, status);
    }

    // Get student name
    public String getStudentName(Long studentId) {
        return userRepository.findById(studentId)
                .map(User::getName)
                .orElse("Unknown Student");
    }

    // Get subject name
    public String getSubjectName(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .map(Subject::getName)
                .orElse("Unknown Subject");
    }

    // Get all requests for a tutor's subjects (for dashboard)
    public List<StudentRequest> getAllRequestsForTutor(Long tutorId) {
        List<StudentRequest> all = studentRequestRepository.findAll();
        List<StudentRequest> result = new ArrayList<>();
        for (StudentRequest req : all) {
            if (req.getSubject() != null && req.getSubject().getTutorSubjects() != null) {
                req.getSubject().getTutorSubjects().forEach(ts -> {
                    if (ts.getTutor() != null && ts.getTutor().getId().equals(tutorId)) {
                        result.add(req);
                    }
                });
            }
        }
        return result;
    }

    // Cancel a request
    public void cancelRequest(Long requestId) {
        StudentRequest request = studentRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
            
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }
        
        studentRequestRepository.deleteById(requestId);
    }
}
