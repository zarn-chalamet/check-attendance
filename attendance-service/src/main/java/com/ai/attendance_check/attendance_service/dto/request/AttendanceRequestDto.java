package com.ai.attendance_check.attendance_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDto {

    private String courseId;
    private String staffId; //staff(lecturer) who started the session
    private LocalDateTime endTime;
}
