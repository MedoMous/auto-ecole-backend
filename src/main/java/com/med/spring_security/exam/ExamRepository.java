package com.med.spring_security.exam;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.user.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam , Long> {
}
