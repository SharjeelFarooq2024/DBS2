package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.StudentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRequestRepository extends JpaRepository<StudentRequest, Long> {
    List<StudentRequest> findByStudentId(Long studentId);
    
    List<StudentRequest> findByStudentIdAndStatus(Long studentId, String status);
    
    List<StudentRequest> findByStatusAndTutorIdOrderByCreatedAtDesc(String status, Long tutorId);
    
    @Query("SELECT sr FROM StudentRequest sr WHERE sr.student.id = :studentId AND sr.status = :status AND sr.tutorId IS NOT NULL")
    List<StudentRequest> findByStudentIdAndStatusWithTutor(@Param("studentId") Long studentId, @Param("status") String status);
    
    @Query("SELECT sr FROM StudentRequest sr WHERE sr.status = 'PENDING'")
    List<StudentRequest> findPendingRequests();
}



