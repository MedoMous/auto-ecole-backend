package com.med.spring_security.user.student;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.exam.Exam;
import com.med.spring_security.exam.ExamRepository;
import com.med.spring_security.exam.examAttempt.ExamAttempt;
import com.med.spring_security.exam.examAttempt.ExamAttemptRepository;
import com.med.spring_security.payment.Payment;
import com.med.spring_security.payment.PaymentRepository;
import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.driving.DrivingSessionRepository;
import com.med.spring_security.session.theory.TheorySession;
import com.med.spring_security.session.theory.TheorySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    public List<Enrollment> getEnrollments(Long id) {
        return enrollmentRepository.findByStudent(
                repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Student not found!")));
    }
    public List<Exam> getMyExams(Long id){
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        List<ExamAttempt> attempts = enrollments.stream()
                .flatMap(enrollment ->
                        examAttemptRepository.findByEnrollment(enrollment).stream())
                .toList();

        return attempts.stream()
                .map(ExamAttempt::getExam)
                .toList();
    }
    public List<ExamAttempt> getMyExamAttempts(Long id){
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with this id:" +id));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        return enrollments.stream()
                .flatMap(enrollment ->
                        examAttemptRepository.findByEnrollment(enrollment).stream())
                .sorted(Comparator.comparing(ExamAttempt::getCreatedAt).reversed())
                .toList();
    }
    public List<Payment> getMyPayments(Long id){
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        return enrollments.stream()
                .flatMap(enrollment -> paymentRepository.findByEnrollment(enrollment).stream())
                .sorted(Comparator.comparing(Payment::getCreatedAt).reversed())
                .toList();
    }
    public List<DrivingSession> getMyDrivingSessions(Long id){
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        return enrollments.stream()
                .flatMap(enrollment ->
                        drivingSessionRepository.findByEnrollment(enrollment)
                                .stream())
                .toList();
    }
    public List<TheorySession> getMyTheorySessions(Long id , Long enrollmentId){
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        if(enrollments.isEmpty()){
            throw new RuntimeException("There's no enrollment with this student id:" + id);
        }
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("There's no enrollment with this id:" + enrollmentId));
        if(!enrollments.contains(enrollment)){
            throw new RuntimeException("There's no enrollment with this id:" + enrollmentId);
        }
        return enrollment.getTheorySessions();

    }
    public DrivingSession registerDrivingSession(DrivingSession session) throws Exception{

        if(drivingSessionRepository.findByScheduledAt(session.getScheduledAt()) != null){
            return drivingSessionRepository.save(session);
        }
        else {
            throw new Exception("Driving session already exists\nPlease chose another schedule");
        }
    }
}