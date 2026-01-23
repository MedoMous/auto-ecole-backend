package com.med.spring_security.payment;

import com.med.spring_security.enrollement.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface PaymentRepository extends JpaRepository<Payment , Long> {

    public List<Payment> findByEnrollment(Enrollment enrollment);
    public List<Payment> findAllByPaymentStatus(PaymentStatus paymentStatus);
}
