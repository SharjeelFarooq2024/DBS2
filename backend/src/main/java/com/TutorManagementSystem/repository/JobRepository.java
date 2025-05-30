package com.TutorManagementSystem.repository;

import com.TutorManagementSystem.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByRequestRequestId(Long requestId);
}

