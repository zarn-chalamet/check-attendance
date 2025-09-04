package com.ai.attendance_check.user_service.service;

import com.ai.attendance_check.user_service.dto.request.UserRequestDto;
import com.ai.attendance_check.user_service.dto.response.UserResponseDto;
import com.ai.attendance_check.user_service.model.UserRole;

import java.util.List;

public interface UserService {
    UserResponseDto createNewUser(UserRequestDto dto);

    List<UserResponseDto> getUserListByRole(UserRole role);

    UserResponseDto getCurrentUserData(String keycloakId);

    UserResponseDto getUserByKeycloakId(String keycloakId);

    UserResponseDto updateUserByKeycloakId(String keycloakId, UserRequestDto dto);

    void deleteUserByKeycloakId(String keycloakId);

    Boolean validateUserByKeycloakId(String keycloakId);
}
