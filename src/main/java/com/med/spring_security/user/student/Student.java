package com.med.spring_security.user.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.enrollement.Enrollment;
import com.med.spring_security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {
    @Column(nullable = false)
    private LocalDateTime birthDate;
    @Column(length = 100 , unique = true)
    private String licenseNumber;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    @OneToMany(
            mappedBy = "student" ,
            cascade = CascadeType.ALL ,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Enrollment> enrollments;
}
