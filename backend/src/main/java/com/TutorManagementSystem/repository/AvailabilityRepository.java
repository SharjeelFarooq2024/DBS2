package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByTutorId(Long tutorId);
    
    // Replace the problematic method with a @Query annotation
    @Query("SELECT COUNT(a) > 0 FROM Availability a WHERE a.tutor.id = :tutorId AND a.subject.subjectId = :subjectId " +
           "AND a.dayOfWeek = :dayOfWeek AND a.timeSlot = :timeSlot")
    boolean existsByTutorAndSubjectAndDayAndTime(
            @Param("tutorId") Long tutorId, 
            @Param("subjectId") Long subjectId, 
            @Param("dayOfWeek") String dayOfWeek, 
            @Param("timeSlot") String timeSlot);
}
