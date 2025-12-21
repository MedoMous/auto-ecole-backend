package com.med.spring_security.enrollement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.exam.ExamStage;
import com.med.spring_security.exam.examAttempt.ExamAttempt;
import com.med.spring_security.license.License;
import com.med.spring_security.payment.Payment;
import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.theory.TheorySession;
import com.med.spring_security.user.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enrollment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "enrollment_seq" ,
            sequenceName = "enrollment_seq_name" ,
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id" ,
            nullable = false
    )
    private Student student;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(nullable = false)
    private LocalDateTime enrollmentDate;

    @Column(nullable = false)
    private int completedTheoryHours;

    @Column(nullable = false)
    private int completedDrivingHours;

    @Column(nullable = false)
    private BigDecimal totalFees;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private BigDecimal remainingAmount;

    private boolean readyForCodeExam = false;
    private boolean readyForParkingExam = false;
    private boolean readyForTrafficExam = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStage currentExamStage;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "enrollment" ,
            cascade = CascadeType.ALL ,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Payment> payments;

    @ManyToMany(
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "theory_session_enrollment",
            joinColumns = @JoinColumn(name = "enrollment_id"),
            inverseJoinColumns = @JoinColumn(name = "theory_session_id"
            )
    )
    @JsonIgnore
    private List<TheorySession> theorySessions;

    @OneToMany(
            mappedBy = "enrollment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<DrivingSession> drivingSessions;

    @ManyToOne(fetch = FetchType.LAZY)
    private License license;

    @OneToMany(
            mappedBy = "enrollment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<ExamAttempt> examAttempts;

}
