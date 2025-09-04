package com.ai.attendance_check.user_service.repository;

import com.ai.attendance_check.user_service.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(@Email @NotBlank String email);

    User findByEmail(@Email @NotBlank String email);
}
