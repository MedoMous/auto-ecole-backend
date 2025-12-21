package com.med.spring_security.user.student;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.exam.ExamRepository;
import com.med.spring_security.exam.examAttempt.ExamAttemptRepository;
import com.med.spring_security.payment.PaymentRepository;
import com.med.spring_security.session.driving.DrivingSessionRepository;
import com.med.spring_security.session.theory.TheorySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DrivingSessionRepository drivingSessionRepository;

    @Autowired
    private TheorySessionRepository theorySessionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    public List<Student> getStudentsWithNotCompletedPayments(){
        List<Enrollment> enrollments = enrollmentRepository.findWithOutStandingPayments();
        return null;
    }
}