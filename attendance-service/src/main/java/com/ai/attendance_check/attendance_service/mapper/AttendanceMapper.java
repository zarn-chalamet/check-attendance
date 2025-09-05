package com.ai.attendance_check.attendance_service.mapper;

import com.ai.attendance_check.attendance_service.dto.response.AttendanceResponseDto;
import com.ai.attendance_check.attendance_service.model.AttendanceSession;

public class AttendanceMapper {

    public static AttendanceResponseDto mapToDto(AttendanceSession session) {

        if(session == null) return null;

        return AttendanceResponseDto.builder()
                .id(session.getId())
                .courseId(session.getCourseId())
                .staffId(session.getStaffId())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .active(session.isActive())
                .records( session.getRecords().stream()
                        .map(
                                user -> AttendanceResponseDto.AttendanceRecordDto
                                        .builder()
                                        .studentId(user.getStudentId())
                                        .checkIn(user.getCheckIn())
                                        .present(user.isPresent())
                                        .late(user.isLate())
                                        .build()

                        ).toList())
                .build();
    }
}
