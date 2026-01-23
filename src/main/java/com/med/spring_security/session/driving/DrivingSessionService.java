package com.med.spring_security.session.driving;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.enrollement.EnrollmentStatus;
import com.med.spring_security.session.SessionStatus;
import com.med.spring_security.session.driving.request.DrivingSessionRequestDTO;
import com.med.spring_security.session.driving.request.DrivingSessionRequestRepository;
import com.med.spring_security.session.driving.request.DrivingSessionRequestStatus;
import com.med.spring_security.user.admin.Admin;
import com.med.spring_security.user.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrivingSessionService {
    @Autowired
    private DrivingSessionRepository drivingSessionRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DrivingSessionRequestRepository repository;

    public DrivingSessionRequestDTO requestDrivingSession(
            Long studentId,
            LocalDateTime scheduledTime,
            String preferredLocation,
            String notes){
        List<Enrollment> enrollments = enrollmentRepository
                .findByStudentIdAndStatus(
                        studentId ,
                        EnrollmentStatus.ACTIVE);
        if(enrollments.isEmpty()){
            throw new RuntimeException("No active enrollments");
        }
        Enrollment enrollment = enrollments.get(0);
        if(scheduledTime.isBefore(LocalDateTime.now().plusHours(24))){
            throw new RuntimeException(
                    "Must request at least 24 hours in advance"
            );
        }
        DrivingSessionRequestDTO request = new DrivingSessionRequestDTO();
        request.setEnrollment(enrollment);
        request.setScheduledAt(scheduledTime);
        request.setPreferredLocation(preferredLocation);
        request.setNotes(notes);
        request.setStatus(DrivingSessionRequestStatus.PENDING);

        return repository.save(request);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public DrivingSession approvedDrivingSession(
            Long requestId,
            Long adminId,
            LocalDateTime finalScheduledTime){
        DrivingSessionRequestDTO request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->  new RuntimeException("Admin not found"));

        if(request.getStatus() != DrivingSessionRequestStatus.PENDING){
            throw new RuntimeException("Request is already processed");
        }
        DrivingSession session = new DrivingSession();
        session.setEnrollment(request.getEnrollment());
        session.setScheduledAt(finalScheduledTime != null ?
                finalScheduledTime : request.getScheduledAt());
        session.setDuration(30);
        session.setStatus(SessionStatus.SCHEDULED);
        session.setCreatedAt(LocalDateTime.now());
        drivingSessionRepository.save(session);

        request.setStatus(DrivingSessionRequestStatus.APPROVED);
        request.setApprovedBy(admin);
        request.setApprovedAt(LocalDateTime.now());
        repository.save(request);
        return session;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void rejectDrivingSessionRequest(
            Long requestId,
            Long adminId,
            String rejectionReason){
        DrivingSessionRequestDTO request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        if(request.getStatus() != DrivingSessionRequestStatus.PENDING){
            throw new RuntimeException("Request is already processed");
        }
        request.setStatus(DrivingSessionRequestStatus.REJECTED);
        request.setRejectionReason(rejectionReason);
        request.setApprovedBy(admin);
        request.setApprovedAt(LocalDateTime.now());
        repository.save(request);
    }
    public void cancelSession(
            Long sessionId,
            Long studentId){
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndStatus(
                studentId ,
                EnrollmentStatus.ACTIVE);
        if(enrollments.isEmpty()){
            throw new RuntimeException("There's no active enrollments");
        }
        Enrollment enrollment = enrollments.get(0);
        DrivingSession session = drivingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found!"));
        if(!enrollment.getDrivingSessions().contains(session)){
            throw new RuntimeException("This session doesn't belong to you");
        }
        session.setStatus(SessionStatus.CANCELLED);
    }
}
