package com.med.spring_security.user.student;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.exam.Exam;
import com.med.spring_security.exam.examAttempt.ExamAttempt;
import com.med.spring_security.payment.Payment;
import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.driving.DrivingSessionService;
import com.med.spring_security.session.driving.request.DrivingSessionRequestDTO;
import com.med.spring_security.session.theory.TheorySession;
import com.med.spring_security.session.theory.TheorySessionRegistrationRequestDTO;
import com.med.spring_security.session.theory.TheorySessionRegistrationResponseDTO;
import com.med.spring_security.session.theory.TheorySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final TheorySessionService theorySessionService;
    private final DrivingSessionService drivingSessionService;

    @Autowired
    public StudentController(
            TheorySessionService theorySessionService,
            StudentService studentService,
            DrivingSessionService drivingSessionService) {
        this.studentService = studentService;
        this.theorySessionService = theorySessionService;
        this.drivingSessionService = drivingSessionService;
    }
    //Student and their Enrollments matters
    @GetMapping("/search/enrollments/{id}")
    public List<Enrollment> getStudentEnrollments(@PathVariable Long id) {
        return studentService.getEnrollments(id);
    }
    @GetMapping("/search/exams/{id}")
    public List<Exam> getStudentExams(@PathVariable Long id){
        return studentService.getMyExams(id);
    }
    @GetMapping("/search/exam_attempts/{id}")
    public List<ExamAttempt> getStudentExamAttempts(@PathVariable Long id){
        return studentService.getMyExamAttempts(id);
    }
    @GetMapping("/search/payments/{id}")
    public List<Payment> getStudentPayments(@PathVariable Long id){
        return studentService.getMyPayments(id);
    }

    //Theory Sessions matters
    @GetMapping("/search/theory_sessions/{id}")
    public List<TheorySession> getTheorySessions(
            @PathVariable Long id){
        return studentService.getMyTheorySessions(id);
    }
    @PostMapping("/theory_sessions")
    public TheorySessionRegistrationResponseDTO registerForTheorySession(
            @RequestParam Long id,
            @RequestBody TheorySessionRegistrationRequestDTO theorySession){
        return theorySessionService.registerForTheorySession(id , theorySession);
    }
    @PutMapping("theory_sessions/{id}/cancel/session")
    public void cancelTheorySession(
            @PathVariable Long id,
            @RequestParam Long sessionId){
        theorySessionService.cancelSession(id , sessionId);
    }
    @PutMapping("/theory_sessions/{id}/schedule/session")
    public TheorySession changeSessionSchedule(
            @PathVariable Long id,
            @RequestParam Long sessionId,
            @RequestParam LocalDateTime scheduledTime){
        return theorySessionService.changeSessionSchedule(id , sessionId , scheduledTime);
    }

    //Driving Session matters
    @GetMapping("/search/driving_sessions/{id}")
    public List<DrivingSession> getStudentDrivingSessions(
            @PathVariable Long id){
        return studentService.getMyDrivingSessions(id);
    }

    @PostMapping("/driving_sessions")
    public DrivingSessionRequestDTO requestDrivingSession(
            @PathVariable Long id,
            @RequestParam LocalDateTime scheduledTime,
            @RequestParam String preferredLocation,
            @RequestParam String notes){
        return drivingSessionService.requestDrivingSession(id, scheduledTime, preferredLocation, notes);
    }
    @PutMapping("driving_sessions/{id}/cancel/session")
    public void cancelDrivingSession(
            @PathVariable Long id,
            @RequestParam Long sessionId){
        drivingSessionService.cancelSession(id , sessionId);
    }
}
