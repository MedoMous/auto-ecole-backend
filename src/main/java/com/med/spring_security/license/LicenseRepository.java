package com.med.spring_security.license;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License , Long> {
}
