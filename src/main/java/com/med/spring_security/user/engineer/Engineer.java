package com.med.spring_security.user.engineer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.spring_security.exam.Exam;
import com.med.spring_security.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue("ENGINEER")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Engineer extends User {

    @Column(
            nullable = false ,
            unique = true
    )
    private String certificationNumber;

    @OneToMany(
            mappedBy = "engineerId",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Exam> exams;
}
