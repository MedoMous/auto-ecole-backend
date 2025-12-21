package com.med.spring_security.enrollement;

import com.med.spring_security.payment.PaymentStatus;
import com.med.spring_security.user.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment , Long> {
    List<Enrollment> findByRemainingAmountGreaterThan(
            BigDecimal remainingAmount,
            EnrollmentStatus status
    );
    List<Enrollment> findWithOutStandingPayments();
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByStatus(EnrollmentStatus status);
}
