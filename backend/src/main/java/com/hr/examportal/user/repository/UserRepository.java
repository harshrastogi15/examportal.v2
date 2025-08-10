package com.hr.examportal.user.repository;

import com.hr.examportal.user.entity.User;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :roleName")
    List<User> findAllByRoleName(String roleName);
}

