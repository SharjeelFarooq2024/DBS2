package com.TutorManagementSystem.repository;

import java.util.List;
import java.util.Optional;
import com.TutorManagementSystem.model.Tutor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    @Query("SELECT DISTINCT t FROM Tutor t " +
           "LEFT JOIN FETCH t.tutorSubjects ts " +
           "LEFT JOIN FETCH ts.subject " +
           "WHERE ts.subject.subjectId = :subjectId")
    List<Tutor> findBySubjectsId(@Param("subjectId") Long subjectId);

    @Query("SELECT t FROM Tutor t " +
           "LEFT JOIN FETCH t.tutorSubjects ts " +
           "LEFT JOIN FETCH ts.subject " +
           "LEFT JOIN FETCH t.availabilities " +
           "WHERE t.id = :tutorId")
    Optional<Tutor> findByIdWithSubjects(@Param("tutorId") Long tutorId);

    // Update this method name to match what's being called
    List<Tutor> findByTutorSubjectsSubjectSubjectId(Long subjectId);
}