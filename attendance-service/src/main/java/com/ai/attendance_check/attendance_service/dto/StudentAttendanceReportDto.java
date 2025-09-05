package com.ai.attendance_check.attendance_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceReportDto {

    private String courseId;
    private String studentId;

    //fetch from User Service
    private String studentName;
    private String email;

    private int totalSessions;      // in the course
    private int attendedSessions;   // sessions where student checked in
    private int missedSessions;     // totalSessions - attendedSessions
    private double attendanceRate;  // (attendedSessions / totalSessions) * 100

    private List<SessionRecord> sessionRecords; // detailed session info

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionRecord {
        private String sessionId;
        private LocalDateTime date;       // ISO string or LocalDate
        private boolean attended;  // true if student was present
        private String status;     // "Present", "Absent", "Late"
    }
}
