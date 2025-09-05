package com.ai.attendance_check.course_service.exception;

public class CourseAlreadyRegisteredException extends RuntimeException{
    public CourseAlreadyRegisteredException(String message) {
        super(message);
    }
}
