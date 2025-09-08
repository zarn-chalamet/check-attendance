package com.ai.attendance_check.attendance_service.service;

import com.ai.attendance_check.attendance_service.dto.AttendanceWarningMessage;

public interface RabbitMqService {

    void publishWarningMessageToRabbitMq(AttendanceWarningMessage message);
}
