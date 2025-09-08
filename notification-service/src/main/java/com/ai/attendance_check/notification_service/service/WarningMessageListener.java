package com.ai.attendance_check.notification_service.service;

import com.ai.attendance_check.notification_service.dto.AttendanceWarningMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarningMessageListener {

    private final EmailService emailService;

    @RabbitListener(queues = "warning.queue")
    public void getMessageAndSendEmail(AttendanceWarningMessage message) {

        log.info("received message: {}", message);
        System.out.println("=========================");
        System.out.println(message);

        //send email using java mail sender
        emailService.sendEmail(message);
    }
}
