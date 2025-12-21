package com.med.spring_security.auth.registerRequest;

import com.med.spring_security.auth.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestStudent extends RegisterRequest {
    private LocalDateTime birthDay;
    private String licenseNumber;
}
