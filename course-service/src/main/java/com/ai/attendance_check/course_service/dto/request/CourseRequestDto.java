package com.ai.attendance_check.course_service.dto.request;

import com.ai.attendance_check.course_service.dto.response.CourseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {

    private String code;
    private String title;
    private String description;

    // Teaching staff with roles
    private List<TeachingStaffDto> staff;

    private List<String> studentIds; //keycloakId list

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean active;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeachingStaffDto {
        private String userId;   // From User service (Keycloak ID)
        private String role;     // "Lecturer", "Assistant Lecturer"
    }
}
