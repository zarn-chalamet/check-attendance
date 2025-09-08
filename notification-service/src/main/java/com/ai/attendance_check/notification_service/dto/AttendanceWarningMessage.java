package com.ai.attendance_check.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceWarningMessage {
    private String studentId;
    private String studentName;
    private String email;
    private String courseId;
    private String courseTitle;
    private double attendanceRate;
}

