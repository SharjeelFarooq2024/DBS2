package com.TutorManagementSystem.service;

import com.TutorManagementSystem.model.Subject;
import com.TutorManagementSystem.model.TutorSubject;
import com.TutorManagementSystem.repository.TutorSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TutorSubjectService {

    @Autowired
    private TutorSubjectRepository tutorSubjectRepository;

    public List<Subject> getSubjectsByTutorId(Long tutorId) {
        List<TutorSubject> tutorSubjects = tutorSubjectRepository.findByTutor_Id(tutorId);

        List<Subject> subjects = new ArrayList<>();
        for (TutorSubject ts : tutorSubjects) {
            subjects.add(ts.getSubject());
        }
        return subjects;
    }
}
