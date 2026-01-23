package com.med.spring_security.session.driving;

import com.med.spring_security.enrollement.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
    public List<DrivingSession> findByEnrollment(Enrollment enrollment);
    public DrivingSession findByScheduledAt(LocalDateTime scheduledAt);
}
