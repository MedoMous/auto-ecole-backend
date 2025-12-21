package com.med.spring_security.auth;

import com.med.spring_security.auth.registerRequest.RegisterRequestAdmin;
import com.med.spring_security.auth.registerRequest.RegisterRequestEngineer;
import com.med.spring_security.auth.registerRequest.RegisterRequestStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/registerStudent")
    public ResponseEntity<AuthenticationResponse> registerStudent(
            @RequestBody RegisterRequestStudent request
    ){
        return ResponseEntity.ok(authenticationService.registerStudent(request));
    }
    @PostMapping("/registerEngineer")
    public ResponseEntity<AuthenticationResponse> registerEngineer(
            @RequestBody RegisterRequestEngineer request
    ){
        return ResponseEntity.ok(authenticationService.registerEngineer(request));
    }
    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody RegisterRequestAdmin request
    ){
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
