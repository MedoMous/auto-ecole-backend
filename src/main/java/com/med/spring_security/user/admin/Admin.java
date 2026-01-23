package com.med.spring_security.user.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.payment.Payment;
import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.driving.request.DrivingSessionRequestDTO;
import com.med.spring_security.session.theory.TheorySession;
import com.med.spring_security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {
    private String certificationNumber;

    @OneToMany(
            mappedBy = "instructor",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    @Column(nullable = false)
    private List<TheorySession> theorySessions;

    @OneToMany(
            mappedBy = "instructor",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    @Column(nullable = false)
    private List<DrivingSession> drivingSessions;

    @OneToMany(
            mappedBy = "validatedBy",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(
            mappedBy = "approvedBy",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<DrivingSessionRequestDTO>  drivingSessionRequestDTOs;

}
