package com.ai.attendance_check.user_service.dto.request;

import com.ai.attendance_check.user_service.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private UserRole role;
}
