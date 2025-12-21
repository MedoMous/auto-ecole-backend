package com.med.spring_security.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.exam.examAttempt.ExamAttempt;
import com.med.spring_security.user.engineer.Engineer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "exam_seq" , sequenceName = "exam_seq_name" , allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ExamStage type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Engineer engineerId;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ExamAttempt> examAttempts;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int max_candidates;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStatus status;

    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;
}
