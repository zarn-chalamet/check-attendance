package com.ai.attendance_check.attendance_service.user;

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
    private String role;
}
