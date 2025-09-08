package com.ai.attendance_check.attendance_service.service.impl;

import com.ai.attendance_check.attendance_service.dto.AttendanceWarningMessage;
import com.ai.attendance_check.attendance_service.service.RabbitMqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqServiceImpl implements RabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queues.name:}")
    private String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    public void publishWarningMessageToRabbitMq(AttendanceWarningMessage attendanceWarningMessage) {
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, attendanceWarningMessage);
        }catch (Exception e) {
            log.error("Failed to publish warning message to RabbitMq",e);
        }
    }
}
