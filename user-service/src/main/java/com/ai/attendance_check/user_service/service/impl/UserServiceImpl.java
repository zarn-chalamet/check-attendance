package com.ai.attendance_check.user_service.service.impl;

import com.ai.attendance_check.user_service.dto.request.UserRequestDto;
import com.ai.attendance_check.user_service.dto.response.UserResponseDto;
import com.ai.attendance_check.user_service.mapper.UserMapper;
import com.ai.attendance_check.user_service.model.User;
import com.ai.attendance_check.user_service.model.UserRole;
import com.ai.attendance_check.user_service.repository.UserRepository;
import com.ai.attendance_check.user_service.service.KeycloakService;
import com.ai.attendance_check.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public UserResponseDto createNewUser(UserRequestDto request) {

        //check user is already register or not
        if(userRepository.existsByEmail(request.getEmail())) {
            User existingUser = userRepository.findByEmail(request.getEmail());

            return UserMapper.mapToDto(existingUser);
        }

        //create in keycloak first
        String keycloakId = keycloakService.createKeycloakUser(request);

        //create new user
        User newUser = User.builder()
                .keycloakId(keycloakId)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
        User createdUser = userRepository.save(newUser);

        return UserMapper.mapToDto(createdUser);
    }

    @Override
    public List<UserResponseDto> getUserListByRole(UserRole role) {
        return List.of();
    }

    @Override
    public UserResponseDto getCurrentUserData(String keycloakId) {
        return null;
    }

    @Override
    public UserResponseDto getUserByKeycloakId(String keycloakId) {
        return null;
    }

    @Override
    public UserResponseDto updateUserByKeycloakId(String keycloakId, UserRequestDto dto) {
        return null;
    }

    @Override
    public void deleteUserByKeycloakId(String keycloakId) {

    }

    @Override
    public Boolean validateUserByKeycloakId(String keycloakId) {
        return null;
    }
}
