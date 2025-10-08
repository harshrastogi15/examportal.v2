package com.hr.examportal.user.repository;

import com.hr.examportal.user.entity.User;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.role = (:roleName)::user_role_enum",nativeQuery = true)
    List<User> findAllByRoleName(String roleName);

    @Query(value = "SELECT EXISTS (" +
            "    SELECT 1 FROM users WHERE id = :userId" +
            ")", nativeQuery = true)
    Optional<Boolean> isUserPresent(UUID userId);
}

