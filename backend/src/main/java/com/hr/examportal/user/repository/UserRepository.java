package com.hr.examportal.user.repository;

import com.hr.examportal.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}

