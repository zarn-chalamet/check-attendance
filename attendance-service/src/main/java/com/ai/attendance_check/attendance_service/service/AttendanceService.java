package com.ai.attendance_check.attendance_service.service;

import com.ai.attendance_check.attendance_service.dto.CourseAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.StudentAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.request.AttendanceRequestDto;
import com.ai.attendance_check.attendance_service.dto.response.AttendanceResponseDto;

import java.util.List;

public interface AttendanceService {
    AttendanceResponseDto startAttendanceSession(AttendanceRequestDto requestDto);

    AttendanceResponseDto markAttendance(String sessionId, String studentId);

    AttendanceResponseDto closeSession(String sessionId);

    List<AttendanceResponseDto> getAllAttendanceSessionByCourseId(String courseId);

    AttendanceResponseDto getAttendanceSectionById(String sessionId);

    StudentAttendanceReportDto getStudentReport(String courseId, String studentId);

    CourseAttendanceReportDto getCourseReport(String courseId);

    void deleteSession(String sessionId);
}
