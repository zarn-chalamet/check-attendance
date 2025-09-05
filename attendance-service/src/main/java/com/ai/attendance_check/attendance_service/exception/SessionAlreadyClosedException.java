package com.ai.attendance_check.attendance_service.exception;

public class SessionAlreadyClosedException extends RuntimeException{
    public SessionAlreadyClosedException(String message) {
        super(message);
    }
}
