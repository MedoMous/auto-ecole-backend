package com.med.spring_security.auth.registerRequest;

import com.med.spring_security.auth.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestAdmin  extends RegisterRequest {
    private String certificationNumber;

}
