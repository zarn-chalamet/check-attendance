package com.ai.attendance_check.user_service.service.impl;

import com.ai.attendance_check.user_service.dto.request.UserRequestDto;
import com.ai.attendance_check.user_service.dto.response.UserResponseDto;
import com.ai.attendance_check.user_service.exception.UserNotFoundException;
import com.ai.attendance_check.user_service.mapper.UserMapper;
import com.ai.attendance_check.user_service.model.User;
import com.ai.attendance_check.user_service.model.UserRole;
import com.ai.attendance_check.user_service.repository.UserRepository;
import com.ai.attendance_check.user_service.service.KeycloakService;
import com.ai.attendance_check.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<User> userList = userRepository.findAllByRole(role);
        System.out.println(userList);

        return userList.stream().map(UserMapper::mapToDto).toList();
    }

    @Override
    public UserResponseDto getCurrentUserData(String keycloakId) {

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found with provided keycloakId."));

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserResponseDto getUserByKeycloakId(String keycloakId) {

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found with provided keycloakId."));

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserResponseDto updateUserByKeycloakId(String keycloakId, UserRequestDto dto) {

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found with provided keycloakId."));

        //update user from keycloak
        keycloakService.updateUser(keycloakId, dto);

        //update user in database
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        User updatedUser = userRepository.save(user);

        return UserMapper.mapToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserByKeycloakId(String keycloakId) {

        boolean userExist = userRepository.existsByKeycloakId(keycloakId);

        System.out.println("---------------------------------------------");
        System.out.println(userExist);

        if(userExist) {
            //delete in keycloak first
            keycloakService.deleteUser(keycloakId);

            //delete in database
            userRepository.deleteByKeycloakId(keycloakId);
        }

    }

    @Override
    public Boolean validateUserByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }
}
