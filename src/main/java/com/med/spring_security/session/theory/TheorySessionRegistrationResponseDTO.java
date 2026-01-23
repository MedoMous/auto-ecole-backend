package com.med.spring_security.session.theory;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheorySessionRegistrationResponseDTO {
    private String message;

    // Session details
    private Long sessionId;
    private LocalDateTime scheduledAt;
    private Integer durationHours;
    private String location;
    private String topic;

    // Instructor
    private String instructorName;

    // Capacity
    private Integer spotsRemaining;

    // Confirmation time
    private LocalDateTime registeredAt;
}
