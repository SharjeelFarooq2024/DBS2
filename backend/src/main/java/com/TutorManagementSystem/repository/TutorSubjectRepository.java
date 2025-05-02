package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.TutorSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TutorSubjectRepository extends JpaRepository<TutorSubject, Long> {
    List<TutorSubject> findByTutor_Id(Long tutorId);
}

