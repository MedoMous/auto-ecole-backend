package com.med.spring_security.user.admin;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.enrollement.EnrollmentStatus;
import com.med.spring_security.payment.PaymentRepository;
import com.med.spring_security.payment.PaymentStatus;
import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.driving.DrivingSessionRepository;
import com.med.spring_security.session.theory.TheorySession;
import com.med.spring_security.session.theory.TheorySessionRepository;
import com.med.spring_security.user.student.Student;
import com.med.spring_security.user.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repository;
    private final StudentRepository studentRepository;
    private final TheorySessionRepository theorySessionRepository;
    private final DrivingSessionRepository drivingSessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public AdminService(
            AdminRepository repository,
            StudentRepository studentRepository,
            TheorySessionRepository  theorySessionRepository,
            DrivingSessionRepository drivingSessionRepository,
            EnrollmentRepository enrollmentRepository,
            PaymentRepository paymentRepository) {
        this.repository = repository;
        this.studentRepository = studentRepository;
        this.theorySessionRepository = theorySessionRepository;
        this.drivingSessionRepository = drivingSessionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Student> findAllStudents(){
        return  studentRepository.findAll();
    }
    public List<Enrollment> findStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentIdAndStatus(studentId , EnrollmentStatus.ACTIVE);
    }
    public List<Student> findStudentsWithUncompletedPayment() {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.NOT_COMPLETED)
                .stream()
                .map(payment -> payment.getEnrollment().getStudent())
                .distinct()
                .toList();
    }
    public List<Student> findStudentsWithCompletedPayment() {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.COMPLETED)
                .stream()
                .map(payment -> payment.getEnrollment().getStudent())
                .distinct()
                .toList();
    }
}
