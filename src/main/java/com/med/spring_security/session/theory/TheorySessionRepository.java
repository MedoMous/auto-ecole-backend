package com.med.spring_security.session.theory;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentStatus;
import com.med.spring_security.user.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TheorySessionRepository extends JpaRepository<TheorySession, Long> {
    TheorySession findByScheduledAt(LocalDateTime scheduledAt);

    @Query("SELECT ts FROM TheorySession ts " +
            "WHERE ts.instructor = :instructor " +
            "AND ts.scheduledAt BETWEEN :start AND :end " +
            "AND ts.status != 'CANCELLED'")
    List<TheorySession> findByInstructorAndScheduledAtBetween(
            @Param("instructor") Admin instructor,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT ts FROM TheorySession ts " +
            "JOIN FETCH ts.enrollments e " +
            "JOIN FETCH e.student s " +
            "WHERE ts.instructor.id = :instructorId " +
            "AND e.status = 'ACTIVE'")
    List<TheorySession> findByInstructorIdWithActiveEnrollment(
            @Param("instructorId") Long instructorId
    );
}
