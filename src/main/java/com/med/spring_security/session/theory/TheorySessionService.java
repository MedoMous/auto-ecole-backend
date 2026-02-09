package com.med.spring_security.session.theory;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.enrollement.EnrollmentRepository;
import com.med.spring_security.enrollement.EnrollmentStatus;
import com.med.spring_security.session.SessionStatus;
import com.med.spring_security.user.admin.Admin;
import com.med.spring_security.user.admin.AdminRepository;
import com.med.spring_security.user.student.Student;
import com.med.spring_security.user.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service

public class TheorySessionService {

    private final TheorySessionRepository repository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public TheorySessionService(
            TheorySessionRepository repository,
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            AdminRepository adminRepository) {
        this.repository = repository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(TheorySessionService.class);

    public TheorySessionRegistrationResponseDTO registerForTheorySession(
            Long studentId,
            TheorySessionRegistrationRequestDTO sessionRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        TheorySession session = repository.findById(sessionRequest.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found!"));

        if(session.getCurrentCapacity() == session.getMaxCapacity()) {
            throw new RuntimeException("Session capacity exceeded!");
        }
        if(session.getScheduledAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Session has been finished");
        }
        if(session.getStatus() == SessionStatus.CANCELLED){
            throw new RuntimeException("Session has been cancelled");
        }
        Enrollment enrollment;
        if(sessionRequest.getEnrollmentId() != null) {
            enrollment = enrollmentRepository
                    .findById(sessionRequest.getEnrollmentId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "There's no Enrollment with this id:"
                                            + sessionRequest.getEnrollmentId()));

            if (!enrollment
                    .getStudent()
                    .getId()
                    .equals(studentId)) {
                throw new RuntimeException("This enrollment is not yours");
            } else {
                List<Enrollment> activeEnrollments = enrollmentRepository
                        .findByStudentAndStatus(student, EnrollmentStatus.ACTIVE);

                if (activeEnrollments.isEmpty()) {
                    throw new RuntimeException("There's no active Enrollment");
                }
                enrollment = activeEnrollments.get(0);
            }

            if(session.getEnrollments().contains(enrollment)){
                throw new RuntimeException("You've already been enrolled to this session");
            }
            List<TheorySession> studentSessions = enrollment.getTheorySessions();
            for(TheorySession existing : studentSessions){
                if(hasTimeConflict(existing ,session)){
                    throw new RuntimeException("You have another session at :" +existing.getScheduledAt());
                }
            }

            session.getEnrollments().add(enrollment);
            session.setCurrentCapacity(session.getMaxCapacity() + 1);
            repository.save(session);
        }
        return TheorySessionRegistrationResponseDTO.builder()
                .message("Successfully registered for theory session!")
                .sessionId(session.getId())
                .scheduledAt(session.getScheduledAt())
                .durationHours(session.getDuration())
                .instructorName(session.getInstructor().getFirstName()
                        + " " +
                        session.getInstructor().getLastName())
                .location(session.getLocation())
                .spotsRemaining(
                        session.getMaxCapacity()
                                -
                                session.getCurrentCapacity())
                .registeredAt(LocalDateTime.now())
                .build();
    }
    public boolean hasTimeConflict(TheorySession existing, TheorySession session){
        LocalDateTime existingStart = existing.getScheduledAt();
        LocalDateTime existingEnd = existingStart.plusMinutes(session.getDuration());
        LocalDateTime newStart = session.getScheduledAt();
        LocalDateTime newEnd = newStart.plusMinutes(session.getDuration());

        return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
    }
    public void cancelSession(
            Long studentId,
            Long sessionId){
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndStatus(
                studentId,
                EnrollmentStatus.ACTIVE);
        if(enrollments.isEmpty()){
            throw new RuntimeException("There is no Enrollment");
        }
        Enrollment enrollment = enrollments.get(0);
        TheorySession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found!"));

        if(!enrollment.getTheorySessions().contains(session)){
            throw new RuntimeException("You've not been enrolled to this session");
        }
        List<Enrollment> sessionEnrollments = session.getEnrollments();
        sessionEnrollments.remove(enrollment);
        session.setEnrollments(sessionEnrollments);
        session.setCurrentCapacity(session.getCurrentCapacity() - 1);
        repository.save(session);
    }
    public TheorySession changeSessionSchedule(
            Long oldSessionId,
            Long studentId,
            LocalDateTime newSchedule){
        TheorySession session = repository.findById(oldSessionId)
                .orElseThrow(() -> new RuntimeException("Session not found!"));
        List<Enrollment> enrollments = enrollmentRepository
                .findByStudentIdAndStatus(
                studentId,
                EnrollmentStatus.ACTIVE);
        if(enrollments.isEmpty()){
            throw new RuntimeException("There is no Enrollment");
        }
        Enrollment enrollment = enrollments.get(0);
        if(!enrollment.getTheorySessions().contains(session)){
            throw new RuntimeException("You've not been enrolled to this session");
        }
        if(repository.findByScheduledAt(newSchedule).getCurrentCapacity()
                ==
                repository.findByScheduledAt(newSchedule).getMaxCapacity()){
            throw new RuntimeException("There's no more seats for this session" +
                    "\n" +
                    "Please chose another schedule or keep the one you have");
        }
        TheorySession newSession = repository.findByScheduledAt(newSchedule);
        newSession.setScheduledAt(newSchedule);
        newSession.setCurrentCapacity(newSession.getCurrentCapacity() + 1);
        repository.save(newSession);
        session.setCurrentCapacity(session.getCurrentCapacity() - 1);
        repository.save(session);
        List<TheorySession> enrollmentSessions = enrollment.getTheorySessions();
        enrollmentSessions.remove(session);
        enrollmentSessions.add(newSession);
        enrollment.setTheorySessions(enrollmentSessions);
        enrollmentRepository.save(enrollment);
        return newSession;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TheorySession moveSessionSchedule(
            Long sessionId,
            Long adminId,
            LocalDateTime newSchedule) {

        // 1. Validate admin exists
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 2. Validate session exists
        TheorySession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // 3. Validate new schedule is in the future
        if (newSchedule.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot schedule session in the past");
        }

        // 4. Check if can still reschedule (48 hours policy)
        if (session.getScheduledAt().minusHours(48).isBefore(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Cannot reschedule less than 48 hours before the session"
            );
        }

        // 5. Check for instructor conflicts at new time
        List<TheorySession> instructorSessions = repository
                .findByInstructorAndScheduledAtBetween(
                        session.getInstructor(),
                        newSchedule.minusMinutes(session.getDuration()),
                        newSchedule.plusMinutes(session.getDuration())
                );

        // Filter out current session from conflicts
        instructorSessions = instructorSessions.stream()
                .filter(s -> !s.getId().equals(sessionId))
                .toList();

        if (!instructorSessions.isEmpty()) {
            throw new RuntimeException(
                    "Instructor has another session at " + instructorSessions.get(0).getScheduledAt()
            );
        }

        // 6. Store old time for notifications
        LocalDateTime oldSchedule = session.getScheduledAt();

        // 7. Update session time (THIS IS THE ONLY PLACE YOU NEED TO SET IT!)
        session.setScheduledAt(newSchedule);
        repository.save(session);
        // 9. Log the change
        log.info("Admin {} rescheduled session {} from {} to {}",
                adminId, sessionId, oldSchedule, newSchedule);

        return session;  // ← Return the SAME session object (now updated)
    }
    @PreAuthorize("hasRole('ADMIN')")
    public TheorySession createTheorySession(TheorySession theorySession){
        return repository.save(theorySession);
    }
}
