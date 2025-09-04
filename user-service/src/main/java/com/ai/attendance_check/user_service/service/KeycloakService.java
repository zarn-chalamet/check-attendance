package com.ai.attendance_check.user_service.service;

import com.ai.attendance_check.user_service.dto.request.UserRequestDto;

public interface KeycloakService {
    String createKeycloakUser(UserRequestDto request);

    void updateUser(String keycloakId, UserRequestDto dto);

    void deleteUser(String keycloakId);
}
