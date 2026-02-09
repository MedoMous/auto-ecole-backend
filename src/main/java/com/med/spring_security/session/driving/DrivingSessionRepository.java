package com.med.spring_security.session.driving;

import com.med.spring_security.enrollement.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
    public List<DrivingSession> findByEnrollment(Enrollment enrollment);
    public DrivingSession findByScheduledAt(LocalDateTime scheduledAt);
    public List<DrivingSession> findByInstructorId(Long instructorId);

    // DrivingSessionRepository
    @Query("SELECT ds FROM DrivingSession ds " +
            "JOIN FETCH ds.enrollment e " +
            "JOIN FETCH e.student s " +
            "WHERE ds.instructor.id = :instructorId " +
            "AND e.status = 'ACTIVE'")
    List<DrivingSession> findByInstructorIdWithStudentAndEnrollment(
            @Param("instructorId") Long instructorId);
}
