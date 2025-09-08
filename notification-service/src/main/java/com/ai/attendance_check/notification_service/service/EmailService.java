package com.ai.attendance_check.notification_service.service;

import com.ai.attendance_check.notification_service.dto.AttendanceWarningMessage;

public interface EmailService {
    void sendEmail(AttendanceWarningMessage attendanceWarningMessage);
}
