package com.med.spring_security.session.theory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheorySessionRegistrationRequestDTO {

    @NonNull
    private Long sessionId;

    private Long enrollmentId;

    private String note;
}
