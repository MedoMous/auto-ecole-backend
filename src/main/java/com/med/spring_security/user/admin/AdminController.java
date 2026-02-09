package com.med.spring_security.user.admin;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.exam.Exam;
import com.med.spring_security.exam.ExamService;
import com.med.spring_security.exam.ExamStage;
import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.driving.DrivingSessionService;
import com.med.spring_security.session.theory.TheorySession;
import com.med.spring_security.session.theory.TheorySessionService;
import com.med.spring_security.user.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
    private final AdminService adminService;
    private final TheorySessionService theorySessionService;
    private final DrivingSessionService drivingSessionService;
    private final ExamService examService;
    
    @Autowired
    AdminController(
            AdminService adminService,
            TheorySessionService  theorySessionService,
            DrivingSessionService drivingSessionService,
            ExamService examService
    ){
        this.adminService = adminService;
        this.drivingSessionService = drivingSessionService;
        this.theorySessionService = theorySessionService;
        this.examService = examService;
    }

    //Students info
    @GetMapping("/students")
    public List<Student> findAllStudents(){
        return adminService.findAllStudents();
    }

    @GetMapping("/enrollments/{id}")
    public List<Enrollment> findStudentEnrollments(@PathVariable Long id){
        return adminService.findStudentEnrollments(id);
    }

    @GetMapping("/payment_status/completed")
    public List<Student> findAllStudentsWithCompletedPayment(){
        return adminService.findAllStudentsWithCompletedPayment();
    }

    @GetMapping("/payment_status/uncompleted")
    public List<Student> findAllStudentsWithUnCompletedPayment(){
        return adminService.findAllStudentsWithUncompletedPayment();
    }
    
    // Admin Info
    @GetMapping()
    public List<Admin> findAllAdmins(){
        return adminService.findAll();
    }
    
    @PutMapping("/{id}")
    public Admin updateAdmin(
            @PathVariable Long id,
            @RequestBody Admin admin){
        return adminService.update(id, admin);
    }
    
    // Theory Session matters
    @GetMapping("/theory_session_schedule/{id}")
    public Map<LocalDateTime , List<Student>> findInstructorTheorySessions(@PathVariable Long id){
        return adminService.findInstructorTheorySessionSchedule(id);
    }
    
    @PutMapping("/theory_sessions/{id}")
    public TheorySession moveSessionSchedule(
            @PathVariable Long id,
            @RequestParam Long adminId , 
            @RequestParam LocalDateTime date){
        return theorySessionService.moveSessionSchedule(id , adminId , date);
    }
    
    @PostMapping("/theory_sessions")
    public TheorySession createTheorySession(@RequestBody TheorySession theorySession){
        return theorySessionService.createTheorySession(theorySession);
    }
    
    // Driving Session matters
    @GetMapping("/driving_session_schedule/{id}")
    public Map<Student , List<LocalDateTime>> findInstructorDrivingSessions(@PathVariable Long id){
        return adminService.findInstructorDrivingSessionSchedule(id);
    }
    
    @PostMapping("/driving_sessions")
    public DrivingSession approveDrivingSession(
            @RequestParam Long requestId,
            @RequestParam Long adminId,
            LocalDateTime finalScheduledTime
    ){
        return drivingSessionService.approvedDrivingSession(requestId, adminId, finalScheduledTime);
    }
    
    @PutMapping("/reject/driving_session")
    public void rejectDrivingSession(
            @RequestParam Long requestId,
            @RequestParam Long adminId,
            String rejectionReason
    ){
        drivingSessionService.rejectDrivingSessionRequest(requestId , adminId , rejectionReason);
    }
    
    // Exam matters
    @GetMapping("/exam_status")
    public Map<ExamStage, List<Student>> getAllStudentsWithExamStatus(){
        return adminService.getAllStudentsWithExamStatus();
    }
    
    @PostMapping("/exams")
    public Exam createExam(@RequestBody Exam exam){
        return examService.createExam(exam);
    }
}
