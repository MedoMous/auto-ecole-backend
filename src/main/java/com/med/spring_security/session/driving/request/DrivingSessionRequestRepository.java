package com.med.spring_security.session.driving.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingSessionRequestRepository extends JpaRepository<DrivingSessionRequestDTO, Long> {
}
