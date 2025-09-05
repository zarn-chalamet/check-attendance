package com.ai.attendance_check.attendance_service.service.impl;

import com.ai.attendance_check.attendance_service.repository.AttendanceSessionRepository;
import com.ai.attendance_check.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
}
