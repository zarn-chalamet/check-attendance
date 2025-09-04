package com.ai.attendance_check.user_service.repository;

import com.ai.attendance_check.user_service.dto.response.UserResponseDto;
import com.ai.attendance_check.user_service.model.User;
import com.ai.attendance_check.user_service.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(@Email @NotBlank String email);

    User findByEmail(@Email @NotBlank String email);

    List<User> findAllByRole(UserRole role);

    Optional<User> findByKeycloakId(String keycloakId);

    boolean existsByKeycloakId(String keycloakId);

    void deleteByKeycloakId(String keycloakId);
}
