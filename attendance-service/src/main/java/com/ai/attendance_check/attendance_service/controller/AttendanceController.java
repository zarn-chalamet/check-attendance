package com.ai.attendance_check.attendance_service.controller;

import com.ai.attendance_check.attendance_service.dto.CourseAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.StudentAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.request.AttendanceRequestDto;
import com.ai.attendance_check.attendance_service.dto.response.AttendanceResponseDto;
import com.ai.attendance_check.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    //start session (by staff)
    @PostMapping("/start")
    public ResponseEntity<AttendanceResponseDto> startSession(@RequestBody AttendanceRequestDto requestDto) {

        AttendanceResponseDto attendanceSession = attendanceService.startAttendanceSession(requestDto);

        return ResponseEntity.ok(attendanceSession);
    }

    //check in (by student)
    @PostMapping("/{sessionId}/check-in")
    public ResponseEntity<AttendanceResponseDto> checkIn(@PathVariable String sessionId,
                                                         @RequestParam("studentId") String studentId) {

        AttendanceResponseDto attendanceSession = attendanceService.markAttendance(sessionId,studentId);

        return ResponseEntity.ok(attendanceSession);
    }

    //close session (by staff)
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<AttendanceResponseDto> endSession(@PathVariable String sessionId) {

        AttendanceResponseDto attendanceSession = attendanceService.closeSession(sessionId);

        return ResponseEntity.ok(attendanceSession);
    }

    //get all attendance sessions by course id
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<List<AttendanceResponseDto>> getAttendanceSessions(@PathVariable String courseId) {

        List<AttendanceResponseDto> sessions = attendanceService.getAllAttendanceSessionByCourseId(courseId);

        return ResponseEntity.ok(sessions);
    }

    //get report of the session(get section by id)
    @GetMapping("/{sessionId}")
    public ResponseEntity<AttendanceResponseDto> getReport(@PathVariable String sessionId) {

        AttendanceResponseDto attendanceSession = attendanceService.getAttendanceSectionById(sessionId);

        return ResponseEntity.ok(attendanceSession);
    }

    //get individual student's attendance report in the course
    @GetMapping("/courses/{courseId}/students/{studentId}/report")
    public ResponseEntity<StudentAttendanceReportDto> getStudentReport(
            @PathVariable String courseId,
            @PathVariable String studentId) {

        StudentAttendanceReportDto report = attendanceService.getStudentReport(courseId, studentId);

        return ResponseEntity.ok(report);
    }

    //get course's aggregate report
    @GetMapping("/courses/{courseId}/report")
    public ResponseEntity<CourseAttendanceReportDto> getCourseReport(@PathVariable String courseId) {

        CourseAttendanceReportDto report = attendanceService.getCourseReport(courseId);

        return ResponseEntity.ok(report);
    }

    //delete attendance session
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable String sessionId) {

        attendanceService.deleteSession(sessionId);

        return ResponseEntity.ok("Session deleted successfully.");
    }


}
