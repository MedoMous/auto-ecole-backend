package com.med.spring_security.user.engineer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface EngineerRepository extends JpaRepository<Engineer, Long> {
}
