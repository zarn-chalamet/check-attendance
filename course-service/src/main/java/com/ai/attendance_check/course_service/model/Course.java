package com.ai.attendance_check.course_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course {

    @Id
    private String id;

    private String code;
    private String title;
    private String description;

    // Teaching staff with roles
    private List<TeachingStaff> staff;

    private List<String> studentIds; //keycloakId list

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean active;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeachingStaff {
        private String userId;   // From User service (Keycloak ID)
        private String role;     // "Lecturer", "Assistant Lecturer"
    }
}
