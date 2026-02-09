package com.med.spring_security.user.engineer;

import com.med.spring_security.exam.Exam;
import com.med.spring_security.exam.ExamStage;
import com.med.spring_security.user.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EngineerService {
    private final EngineerRepository engineerRepository;

    @Autowired
    public EngineerService(
            EngineerRepository engineerRepository) {
        this.engineerRepository = engineerRepository;
    }

    public Map<ExamStage, List<Student>> getInstructorSchedule(Long id) {
        Engineer engineer = engineerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Engineer not found"));
        return engineer.getExams().stream()
                .collect(Collectors.groupingBy(Exam::getType,
                        Collectors.flatMapping(exam -> exam.getExamAttempts()
                                        .stream().map(
                                                examAttempt -> examAttempt.getEnrollment().getStudent()),
                                Collectors.toList())
                ));
    }
    public Map<LocalDateTime, String> getInstructorScheduleLocations(Long id){
        Engineer  engineer = engineerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Engineer not found"));

        return engineer.getExams().stream()
                .collect(Collectors.toMap(
                        Exam::getScheduledAt ,
                        Exam::getLocation
                ));
    }
}