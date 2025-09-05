package com.ai.attendance_check.attendance_service.exception;

public class StudentAlreadyCheckInException extends RuntimeException{
    public StudentAlreadyCheckInException(String message) {
        super(message);
    }
}
