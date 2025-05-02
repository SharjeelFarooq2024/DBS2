package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByNameIgnoreCase(String name);
    
    @Query("SELECT s FROM Subject s " +
           "LEFT JOIN FETCH s.studentSubjects " +
           "WHERE s.subjectId = :subjectId")
    Optional<Subject> findByIdWithStudents(@Param("subjectId") Long subjectId);
    
    @Query("SELECT DISTINCT s FROM Subject s " +
           "JOIN s.studentSubjects ss " +
           "WHERE ss.student.id = :studentId")
    List<Subject> findByStudentId(@Param("studentId") Long studentId);

    List<Subject> findBySubjectIdIn(List<Long> ids);

    @Query("SELECT s FROM Subject s " +
           "JOIN s.tutorSubjects ts " +
           "WHERE ts.tutor.id = :tutorId")
    List<Subject> findSubjectsByTutorId(@Param("tutorId") Long tutorId);
}

