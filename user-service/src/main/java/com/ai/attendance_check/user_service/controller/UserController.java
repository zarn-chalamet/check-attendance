package com.ai.attendance_check.user_service.controller;

import com.ai.attendance_check.user_service.dto.request.UserRequestDto;
import com.ai.attendance_check.user_service.dto.response.UserResponseDto;
import com.ai.attendance_check.user_service.model.UserRole;
import com.ai.attendance_check.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create new user (only admin)
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewUser(dto));
    }

    // Get users by role (only admin)
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(@PathVariable("role") UserRole role) {

        return ResponseEntity.ok(userService.getUserListByRole(role));
    }

    // Get current user
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getCurrentUser(@RequestHeader("X-User-ID") String keycloakId) {

        return ResponseEntity.ok(userService.getCurrentUserData(keycloakId));
    }

    // Get user by ID (Keycloak ID)
    @GetMapping("/{keycloakId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String keycloakId) {

        return ResponseEntity.ok(userService.getUserByKeycloakId(keycloakId));
    }

    // Update user (only admin)
    @PutMapping("/{keycloakId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String keycloakId,
                                                      @RequestBody UserRequestDto dto) {

        return ResponseEntity.ok(userService.updateUserByKeycloakId(keycloakId, dto));
    }

    // Delete user (only admin)
    @DeleteMapping("/{keycloakId}")
    public ResponseEntity<?> deleteUser(@PathVariable String keycloakId) {
        userService.deleteUserByKeycloakId(keycloakId);

        return ResponseEntity.ok("User deleted successfully.");
    }

    // Validate user
    @GetMapping("/{keycloakId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String keycloakId) {

        return ResponseEntity.ok(userService.validateUserByKeycloakId(keycloakId));
    }
}

