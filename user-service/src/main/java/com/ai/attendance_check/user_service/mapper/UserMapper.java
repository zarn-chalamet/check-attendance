package com.ai.attendance_check.user_service.mapper;

import com.ai.attendance_check.user_service.dto.response.UserResponseDto;
import com.ai.attendance_check.user_service.model.User;

public class UserMapper {

    public static UserResponseDto mapToDto(User user) {

        if(user == null) return null;

        return UserResponseDto.builder()
                .id(user.getId())
                .keycloakId(user.getKeycloakId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
