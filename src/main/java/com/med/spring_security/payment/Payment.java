package com.med.spring_security.payment;

import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.user.admin.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "payment_seq" ,
            sequenceName = "payment_seq_name" ,
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "enrollment_id" ,
            nullable = false
    )
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin validatedBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime validatedAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
