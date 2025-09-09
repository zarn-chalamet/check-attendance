package com.ai.attendance_check.attendance_service.dto.response;

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
public class AttendanceResponseDto {
    private String id;

    private String courseId;
    private String staffId; //staff(lecturer) who started the session

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean active;

    private List<AttendanceRecordDto> records;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendanceRecordDto {
        private String studentId; //keycloak Id
        private LocalDateTime checkIn; // checked in time
        private boolean present; // True if within cutoff, false if absent
        private boolean late; // If after cutoff but allowed
    }
}
