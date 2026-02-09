package com.med.spring_security.user.student;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.exam.ExamStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student , Long> {
    @Query("SELECT stu FROM Student stu " +
            "JOIN FETCH stu.enrollments e " +
            "WHERE e.status = 'ACTIVE'")
    List<Student> findAllByActiveEnrollment();

//    @Query("SELECT DISTINCT stu FROM Student stu " +
//            "JOIN FETCH stu.enrollments e " +
//            "JOIN FETCH e.currentExamStage ces " +
//            "JOIN FETCH e.examAttempts ea " +
//            "JOIN FETCH ea.engineer eng " +
//            "WHERE ces = :examStage " +
//            "AND eng.id = :engineerID")
//    public List<Student> findAllByExamStageAndEngineerId(
//            @Param("examStage") ExamStage examStage,
//            @Param("engineerID") Long engineerID
//    );
}
