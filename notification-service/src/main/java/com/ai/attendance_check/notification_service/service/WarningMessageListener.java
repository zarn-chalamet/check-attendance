package com.ai.attendance_check.notification_service.service;

import com.ai.attendance_check.notification_service.dto.AttendanceWarningMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarningMessageListener {

    public void getMessageAndSendEmail(AttendanceWarningMessage message) {

        log.info("received message: {}", message);

        //send email using java mail sender
    }
}
