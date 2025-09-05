package com.ai.attendance_check.attendance_service.service.impl;

import com.ai.attendance_check.attendance_service.dto.CourseAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.StudentAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.request.AttendanceRequestDto;
import com.ai.attendance_check.attendance_service.dto.response.AttendanceResponseDto;
import com.ai.attendance_check.attendance_service.exception.AttendanceSessionNotFoundException;
import com.ai.attendance_check.attendance_service.mapper.AttendanceMapper;
import com.ai.attendance_check.attendance_service.model.AttendanceSession;
import com.ai.attendance_check.attendance_service.repository.AttendanceSessionRepository;
import com.ai.attendance_check.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private static final int CUTOFF_MINUTES = 30;

    @Override
    public AttendanceResponseDto startAttendanceSession(AttendanceRequestDto requestDto) {

        //create attendance session
        AttendanceSession session = AttendanceSession.builder()
                .courseId(requestDto.getCourseId())
                .staffId(requestDto.getStaffId())
                .startTime(LocalDateTime.now())
                .endTime(requestDto.getEndTime())
                .active(true)
                .records(new ArrayList<>())
                .build();

        //save user in the session
        AttendanceSession createdSession = attendanceSessionRepository.save(session);

        return AttendanceMapper.mapToDto(createdSession);
    }

    @Override
    public AttendanceResponseDto markAttendance(String sessionId, String studentId) {

        //get session by id
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AttendanceSessionNotFoundException("Attendance section not found with id: "+ sessionId));

        //check session is active or not
        if (!session.isActive()) {
            throw new RuntimeException("Session already closed");
        }

        //session logic
        LocalDateTime now = LocalDateTime.now();
        boolean present = now.isBefore(session.getStartTime().plusMinutes(CUTOFF_MINUTES));
        boolean late = !present;

        //set attendance record
        AttendanceSession.AttendanceRecord record = AttendanceSession.AttendanceRecord
                .builder()
                .studentId(studentId)
                .checkIn(now)
                .present(present)
                .late(late)
                .build();

        session.getRecords().add(record);

        //save to repository
        AttendanceSession updatedSession = attendanceSessionRepository.save(session);

        return AttendanceMapper.mapToDto(updatedSession);
    }

    @Override
    public AttendanceResponseDto closeSession(String sessionId) {

        //get session by id
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AttendanceSessionNotFoundException("Attendance section not found with id: "+ sessionId));

        session.setActive(false);

        //save to repository
        AttendanceSession updatedSession = attendanceSessionRepository.save(session);

        return AttendanceMapper.mapToDto(updatedSession);
    }

    @Override
    public List<AttendanceResponseDto> getAllAttendanceSessionByCourseId(String courseId) {

        List<AttendanceSession> sessions = attendanceSessionRepository.findAllByCourseId(courseId);

        return sessions.stream()
                .map(AttendanceMapper::mapToDto)
                .toList();
    }

    @Override
    public AttendanceResponseDto getAttendanceSectionById(String sessionId) {

        //get session by id
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AttendanceSessionNotFoundException("Attendance section not found with id: "+ sessionId));

        return AttendanceMapper.mapToDto(session);
    }

    @Override
    public StudentAttendanceReportDto getStudentReport(String courseId, String studentId) {

        //get user data from user-service

        //get course data from course-service

        return null;
    }

    @Override
    public CourseAttendanceReportDto getCourseReport(String courseId) {
        return null;
    }

    @Override
    public void deleteSession(String sessionId) {

        boolean existSession = attendanceSessionRepository.existsById(sessionId);

        if(existSession) {
            attendanceSessionRepository.deleteById(sessionId);
        }
    }
}
