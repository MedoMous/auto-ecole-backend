package com.med.spring_security.exam.examAttempt;

import com.med.spring_security.enrollement.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    public List<ExamAttempt> findByEnrollment(Enrollment enrollment);
}
