package com.med.spring_security.session.theory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.session.SessionStatus;
import com.med.spring_security.user.admin.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "theory_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheorySession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "theory_session_seq" ,
            sequenceName = "theory_session_seq_name" ,
            allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin instructor;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private int currentCapacity = 0;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToMany(mappedBy = "theorySessions")
    @JsonIgnore
    private List<Enrollment> enrollments;
}
