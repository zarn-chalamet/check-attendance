package com.ai.attendance_check.attendance_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AttendanceSessionNotFoundException.class)
    public ProblemDetail handleAttendanceSessionNotFoundException(AttendanceSessionNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(StudentAlreadyCheckInException.class)
    public ProblemDetail handleStudentAlreadyCheckInException(StudentAlreadyCheckInException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SessionAlreadyClosedException.class)
    public ProblemDetail handleSessionAlreadyClosedException(SessionAlreadyClosedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
