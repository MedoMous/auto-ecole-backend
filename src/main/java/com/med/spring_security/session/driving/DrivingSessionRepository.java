package com.med.spring_security.session.driving;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
}
