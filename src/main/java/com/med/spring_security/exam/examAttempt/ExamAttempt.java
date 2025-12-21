package com.med.spring_security.exam.examAttempt;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.exam.Exam;
import com.med.spring_security.exam.ExamStage;
import com.med.spring_security.user.engineer.Engineer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "exam_att_seq" ,
            sequenceName = "exam_att_seq_name" ,
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "exam_id",
            nullable = false
    )
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "enrollment_id",
            nullable = false
    )
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evualted_by_engineer_id")
    private Engineer engineer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStage examType;

    @Column(nullable = false)
    private int attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamResult result;

    @Column(nullable = false)
    private float score;

    @Column(columnDefinition = "TEXT")
    private String engineerFeedback;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime evaluatedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;
}
