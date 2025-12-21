package com.med.spring_security.license;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.enrollement.Enrollment;
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
@Table(name = "license_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "license_seq" ,
            sequenceName = "license_seq_name" ,
            allocationSize = 1
    )
    private Long id;

    @OneToMany(
            mappedBy = "license",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Enrollment> enrollments;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LicenseType type;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(
            columnDefinition = "TEXT" ,
            length = 150
    )
    private String description;

    @Column(nullable = false)
    private int requiredTheoryHours = 20;

    @Column(nullable = false)
    private int requiredDrivingHours = 20;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
