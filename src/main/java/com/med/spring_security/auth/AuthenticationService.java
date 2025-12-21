package com.med.spring_security.auth;

import com.med.spring_security.auth.registerRequest.RegisterRequestAdmin;
import com.med.spring_security.auth.registerRequest.RegisterRequestEngineer;
import com.med.spring_security.auth.registerRequest.RegisterRequestStudent;
import com.med.spring_security.config.JwtService;
import com.med.spring_security.user.*;
import com.med.spring_security.user.admin.Admin;
import com.med.spring_security.user.engineer.Engineer;
import com.med.spring_security.user.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerStudent(
            RegisterRequestStudent request){
        var user = Student.builder()
                .username(request.getEmail())
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDay())
                .role(UserRole.USER)
                .enabled(true)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticationResponse registerAdmin(
            RegisterRequestAdmin request){
        var user = Admin.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .certificationNumber(request.getCertificationNumber())
                .role(UserRole.ADMIN)
                .enabled(true)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerEngineer(
            RegisterRequestEngineer request){
        var user = Engineer.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .certificationNumber(request.getCertificationNumber())
                .role(UserRole.ENGINEER)
                .enabled(true)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticationResponse registerAdmin(
            RegisterRequest request)
    {
        var user = Admin.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(UserRole.ADMIN)
                .enabled(true)
                .build();
        return null;
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest request
    ){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
