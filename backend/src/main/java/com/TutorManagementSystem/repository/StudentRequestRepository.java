package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.StudentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRequestRepository extends JpaRepository<StudentRequest, Long> {

    // Find requests by subject's subjectId
    List<StudentRequest> findBySubject_SubjectId(Long subjectId);

    // Find all requests by status
    List<StudentRequest> findByStatus(String status);

    @Query("SELECT r FROM StudentRequest r WHERE r.status = :status AND r.subject.subjectId IN " +
           "(SELECT ts.subject.subjectId FROM TutorSubject ts WHERE ts.tutor.id = :tutorId) " +
           "ORDER BY r.createdAt DESC")
    List<StudentRequest> findByStatusAndTutorIdOrderByCreatedAtDesc(
        @Param("status") String status, 
        @Param("tutorId") Long tutorId
    );

    @Query("SELECT r FROM StudentRequest r WHERE r.status = 'PENDING'")
    List<StudentRequest> findPendingRequests();

    // Find all requests by student id
    List<StudentRequest> findByStudentId(Long studentId);

    // Find all requests by student id and status
    List<StudentRequest> findByStudentIdAndStatus(Long studentId, String status);

}



