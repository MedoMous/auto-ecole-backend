package com.med.spring_security.session.driving;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.session.SessionStatus;
import com.med.spring_security.user.admin.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "driving_session")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "driving_session_seq" ,
            sequenceName = "driving_session_seq_name" ,
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin instructor;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false , length = 100)
    private String vehicleInfo;
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(nullable = false , length = 150)
    private String startingLocation;

    @Column(length = 150)
    private String routesNotes;

    @Column(columnDefinition = "TEXT")
    private String instructorFeedback;

    @Column(nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private Date updatedAt;
}
