package com.med.spring_security.exam;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.exam.examAttempt.ExamAttempt;
import com.med.spring_security.exam.examAttempt.ExamAttemptRepository;
import com.med.spring_security.exam.examAttempt.ExamResult;
import com.med.spring_security.user.engineer.Engineer;
import com.med.spring_security.user.engineer.EngineerRepository;
import com.med.spring_security.user.student.Student;
import com.med.spring_security.user.student.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final EngineerRepository engineerRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ExamAttemptRepository examAttemptRepository;

    @Autowired
    ExamService(
            ExamRepository examRepository,
            EngineerRepository engineerRepository,
            EnrollmentRepository enrollmentRepository,
            ExamAttemptRepository examAttemptRepository) {
        this.examRepository = examRepository;
        this.engineerRepository = engineerRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.examAttemptRepository = examAttemptRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Exam createExam(Exam exam){
        return examRepository.save(exam);
    }

    @PreAuthorize("hasRole('Engineer')")
    public Exam examResult(
            Long examId,
            Long engineerId,
            Long studentId,
            Long enrollmentId,
            float score,
            String feedback
    ){
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        Engineer engineer = engineerRepository.findById(engineerId)
                .orElseThrow(() -> new RuntimeException("Engineer not found"));
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Enrollment doesn't belong to this student");
        }

        ExamAttempt examAttempt = ExamAttempt.builder()
                .examType(enrollment.getCurrentExamStage())
                .result(score > 15f ? ExamResult.PASSED : ExamResult.FAILED)
                .attemptNumber(exam.getExamAttempts().size() + 1)
                .score(score)
                .engineerFeedback(feedback)
                .exam(exam)
                .engineer(engineer)
                .evaluatedAt(LocalDateTime.now())
                .build();

        if(score > 15f) {
            exam.setStatus(ExamStatus.COMPLETED);
            if (enrollment.getCurrentExamStage() == ExamStage.CODE_RULES)
                enrollment.setCurrentExamStage(ExamStage.PARKING);
            else if (enrollment.getCurrentExamStage() == ExamStage.PARKING)
                enrollment.setCurrentExamStage(ExamStage.TRAFFIC);
            else enrollment.setCurrentExamStage(ExamStage.COMPLETED);
        }

        examAttemptRepository.save(examAttempt);
        enrollment.getExamAttempts().add(examAttempt);
        enrollmentRepository.save(enrollment);
        exam.getExamAttempts().add(examAttempt);

        return examRepository.save(exam);
    }
}
