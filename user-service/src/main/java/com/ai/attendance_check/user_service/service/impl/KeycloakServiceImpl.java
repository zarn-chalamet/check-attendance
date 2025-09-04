package com.ai.attendance_check.user_service.service.impl;

import com.ai.attendance_check.user_service.dto.request.UserRequestDto;
import com.ai.attendance_check.user_service.service.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;
    private static final String REALM = "attendance-checkin";

    public KeycloakServiceImpl() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8181") // docker port
                .realm("master") // login realm for admin
                .clientId("admin-cli")
                .username("zarn") // admin username
                .password("zarn1234") // admin password
                .build();
    }

    @Override
    public String createKeycloakUser(UserRequestDto userDto) {

        // Create new user
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEnabled(true);

        // Create password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(true);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("user1234"); //general password

        user.setCredentials(List.of(passwordCred));

        // Target your app realm (attendance-checkin)
        UsersResource usersResource = keycloak.realm(REALM).users();

        // Create user (no Response handling)
        usersResource.create(user);

        // Fetch created user by email
        List<UserRepresentation> users = usersResource.search(userDto.getEmail());
        System.out.println("=========================================");
        System.out.println(users);
        if (users.isEmpty()) {
            throw new RuntimeException("User not found after creation: " + userDto.getEmail());
        }
        String keycloakId = users.get(0).getId();
        System.out.println("=========================================");
        System.out.println(keycloakId);

        // Assign role
        keycloak.realm(REALM)
                .users()
                .get(keycloakId)
                .roles()
                .realmLevel()
                .add(List.of(
                        keycloak.realm(REALM)
                                .roles()
                                .get(userDto.getRole().name())
                                .toRepresentation()
                ));

        return keycloakId;
    }

    @Override
    public void updateUser(String keycloakId, UserRequestDto dto) {
        RealmResource realmResource = keycloak.realm(REALM);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = usersResource.get(keycloakId).toRepresentation();

        // Update fields
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getEmail());

        // Push update back to Keycloak
        usersResource.get(keycloakId).update(user);

        // Optional: update role if changed
        if (dto.getRole() != null) {
            // remove all existing roles first
            var roleMappings = usersResource.get(keycloakId).roles().realmLevel().listAll();
            usersResource.get(keycloakId).roles().realmLevel().remove(roleMappings);

            // assign new role
            usersResource.get(keycloakId).roles().realmLevel()
                    .add(List.of(realmResource.roles().get(dto.getRole().name()).toRepresentation()));
        }
    }

    @Override
    public void deleteUser(String keycloakId) {
        try {
            keycloak.realm(REALM)
                    .users()
                    .get(keycloakId)
                    .remove();
            System.out.println("User deleted successfully: " + keycloakId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user with ID: " + keycloakId, e);
        }
    }
}
