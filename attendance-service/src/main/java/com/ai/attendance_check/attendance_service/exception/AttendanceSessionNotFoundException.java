package com.ai.attendance_check.attendance_service.exception;

public class AttendanceSessionNotFoundException extends RuntimeException{
    public AttendanceSessionNotFoundException(String message) {
        super(message);
    }
}
