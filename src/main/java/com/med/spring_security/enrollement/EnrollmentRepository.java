package com.med.spring_security.enrollement;

import com.med.spring_security.user.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment , Long> {
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByStudentAndStatus(
            Student student,
            EnrollmentStatus status);
    List<Enrollment> findByStudentIdAndStatus(
            Long studentId ,
            EnrollmentStatus status);
}
