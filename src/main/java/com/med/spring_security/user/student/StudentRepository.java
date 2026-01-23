package com.med.spring_security.user.student;

import com.med.spring_security.enrollement.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student , Long> {
    public Student findByEnrollment(Enrollment enrollment);
}
