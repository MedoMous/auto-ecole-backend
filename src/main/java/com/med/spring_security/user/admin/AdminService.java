package com.med.spring_security.user.admin;

import com.med.spring_security.session.driving.DrivingSession;
import com.med.spring_security.session.theory.TheorySession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository repository;

}
