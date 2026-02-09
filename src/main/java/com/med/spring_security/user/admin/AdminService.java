package com.med.spring_security.user.admin;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.enrollement.EnrollmentStatus;
import com.med.spring_security.exam.ExamStage;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Admin> findAll(){
        return repository.findAll();
    }

    public Admin update(Long id , Admin admin){
        Admin newAdmin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin Not Found"));
        newAdmin.setFirstName(admin.getFirstName());
        newAdmin.setLastName(admin.getLastName());
        newAdmin.setEmail(admin.getEmail());
        newAdmin.setPassword(admin.getPassword());
        newAdmin.setTheorySessions(admin.getTheorySessions());
        newAdmin.setDrivingSessions(admin.getDrivingSessions());
        newAdmin.setCertificationNumber(admin.getCertificationNumber());
        return repository.save(newAdmin);
    }

    public List<Student> findAllStudents(){
        return  studentRepository.findAll();
    }
    public List<Enrollment> findStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentIdAndStatus(studentId , EnrollmentStatus.ACTIVE);
    }
    public List<Student> findAllStudentsWithUncompletedPayment() {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.NOT_COMPLETED)
                .stream()
                .map(payment -> payment.getEnrollment().getStudent())
                .distinct()
                .toList();
    }
    public List<Student> findAllStudentsWithCompletedPayment() {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.COMPLETED)
                .stream()
                .map(payment -> payment.getEnrollment().getStudent())
                .distinct()
                .toList();
    }
    public Map<Student , List<LocalDateTime>> findInstructorDrivingSessionSchedule(Long adminId){
        List<DrivingSession> drivingSessions = drivingSessionRepository.findByInstructorIdWithStudentAndEnrollment(
                adminId);
        return drivingSessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getEnrollment().getStudent(),
                        Collectors.mapping(
                                DrivingSession::getScheduledAt,
                                Collectors.toList()
                        )
                ));

    }
    public Map<LocalDateTime, List<Student>> findInstructorTheorySessionSchedule(Long adminId){
        List<TheorySession> theorySessions = theorySessionRepository.findByInstructorIdWithActiveEnrollment(adminId);
        return theorySessions.stream()
                .collect(Collectors.groupingBy(
                        TheorySession::getScheduledAt,
                        Collectors.flatMapping(
                                session -> session.getEnrollments()
                                        .stream()
                                        .map(Enrollment::getStudent),
                                Collectors.toList()
                        )
                ));
    }
    public Map<ExamStage , List<Student>> getAllStudentsWithExamStatus(){
        List<Student> students = studentRepository.findAllByActiveEnrollment();

        return students.stream()
                .flatMap(student -> student.getEnrollments().stream()
                        .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                        .map(e -> Map.entry(e.getCurrentExamStage() , student)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                Map.Entry::getValue,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream().distinct().toList()
                                )
                        )
                ));
    }
}
