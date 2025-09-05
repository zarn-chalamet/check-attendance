package com.ai.attendance_check.attendance_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAttendanceReportDto {

    private String courseId;
    private String courseCode;
    private String courseTitle; // fetch from course-service

    private int totalSessions;     // how many sessions conducted
    private int totalStudents;     // enrolled students
    private double averageAttendanceRate; // avg across all students

    private List<StudentSummary> studentSummaries; // per-student breakdown

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentSummary {
        private String studentId;
        private String studentName;
        private int attendedSessions;
        private int missedSessions;
        private double attendanceRate; // percentage
    }
}
