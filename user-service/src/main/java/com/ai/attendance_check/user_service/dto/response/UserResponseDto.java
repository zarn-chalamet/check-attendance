package com.ai.attendance_check.user_service.dto.response;

import com.ai.attendance_check.user_service.model.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private String id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
}
