package com.med.spring_security.user.engineer;

import com.med.spring_security.exam.Exam;
import com.med.spring_security.exam.ExamService;
import com.med.spring_security.exam.ExamStage;
import com.med.spring_security.user.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/engineers")
public class EngineerController {

    private final EngineerService engineerService;
    private final ExamService examService;

    @Autowired
    public EngineerController(
            EngineerService engineerService,
            ExamService examService) {
        this.engineerService = engineerService;
        this.examService = examService;
    }
    @GetMapping("/{id}")
    public Map<LocalDateTime , String> getInstructorScheduleLocations(@PathVariable Long id){
        return engineerService.getInstructorScheduleLocations(id);
    }

    @GetMapping("/students/{id}")
    public Map<ExamStage , List<Student>> getInstructorSchedule(@PathVariable Long id){
        return engineerService.getInstructorSchedule(id);
    }

    @PostMapping("/exams/{id}")
    public Exam examResult(
            @PathVariable Long id,
            @RequestParam Long adminId,
            @RequestParam Long studentId,
            @RequestParam Long enrollmentId,
            float score,
            String feedback){
        return examService.examResult(id, adminId, studentId, enrollmentId, score, feedback);
    }

}
