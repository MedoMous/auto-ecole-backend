package com.med.spring_security.session.driving.request;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.user.admin.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "driving_session_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingSessionRequestDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "dri_session_req_seq" ,
            sequenceName = "dri_session_req_seq_name",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Enrollment enrollment;

    private DrivingSessionRequestStatus status;

    private LocalDateTime scheduledAt;

    private String preferredLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin approvedBy;

    private LocalDateTime approvedAt;
    private String rejectionReason;
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
