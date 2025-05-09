package com.TutorManagementSystem.service;

import java.util.List;
import java.util.Optional;

import com.TutorManagementSystem.model.Job;
import com.TutorManagementSystem.model.Availability;
import com.TutorManagementSystem.repository.JobRepository;
import com.TutorManagementSystem.repository.StudentRequestRepository;
import com.TutorManagementSystem.repository.TutorRepository;
import com.TutorManagementSystem.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private StudentRequestRepository requestRepo;

    @Autowired
    private TutorRepository tutorRepo;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    /**
     * Assigns a tutor to an existing request:
     *  - Updates the request's status to "ASSIGNED"
     *  - Sets the job's assignment timestamp
     *  - Sets the job's start and end time based on availability
     *  - Saves both entities to the DB
     */
    public Job assignJob(Job job) {
        // Mark the linked request as assigned
        requestRepo.findById(job.getRequest().getRequestId()).ifPresent(req -> {
            req.setStatus("ACCEPTED");
            requestRepo.save(req);
        });

        // Set start and end time based on the first matching availability slot
        if (job.getRequest() != null && job.getTutorId() != null) {
            List<Availability> slots = availabilityRepository.findByTutorId(job.getTutorId());
            Availability matched = null;
            for (Availability slot : slots) {
                if (slot.getSubject().getSubjectId().equals(job.getRequest().getSubject().getSubjectId())) {
                    matched = slot;
                    break;
                }
            }
            if (matched != null) {
                // Parse hour from timeSlot (e.g., "10:00 AM")
                String timeSlot = matched.getTimeSlot();
                int hour = 9; // default
                boolean isPM = timeSlot.toUpperCase().contains("PM");
                try {
                    String[] parts = timeSlot.split(":| ");
                    hour = Integer.parseInt(parts[0]);
                    if (isPM && hour < 12) hour += 12;
                    if (!isPM && hour == 12) hour = 0;
                } catch (Exception e) { /* fallback to 9 */ }
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                java.time.LocalDateTime start = now.withHour(hour).withMinute(0).withSecond(0).withNano(0);
                if (start.isBefore(now)) start = start.plusDays(1); // next day if already passed
                job.setStartTime(start);
                job.setEndTime(start.plusHours(1));
            }
        }
        job.setAssignedAt(LocalDateTime.now());
        return jobRepo.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    public Optional<Job> findByRequestId(Long requestId) {
        return jobRepo.findByRequestRequestId(requestId);
    }

    public String getTutorName(Long tutorId) {
        return tutorRepo.findById(tutorId)
            .map(tutor -> tutor.getName())  // Use tutor.getName() directly instead of tutor.getUser().getName()
            .orElse("Unknown Tutor");
    }
}