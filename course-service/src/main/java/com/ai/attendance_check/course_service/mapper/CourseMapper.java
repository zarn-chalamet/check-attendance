package com.ai.attendance_check.course_service.mapper;

import com.ai.attendance_check.course_service.dto.request.CourseRequestDto;
import com.ai.attendance_check.course_service.dto.response.CourseResponseDto;
import com.ai.attendance_check.course_service.model.Course;

import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper {

    public static CourseResponseDto mapToDto(Course course) {
        if (course == null) {
            return null;
        }

        return CourseResponseDto.builder()
                .id(course.getId())
                .code(course.getCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .active(course.isActive())
                .studentIds(course.getStudentIds())
                .staff(mapStaffToDto(course.getStaff()))
                .build();
    }

    public static Course dtoToModel(CourseRequestDto dto) {

        if (dto == null) return null;

        return Course.builder()
                .code(dto.getCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .staff(dtoStaffToModel(dto.getStaff()))
                .studentIds(dto.getStudentIds())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .active(dto.isActive())
                .build();
    }

    private static List<CourseResponseDto.TeachingStaffDto> mapStaffToDto(List<Course.TeachingStaff> staff) {

        if (staff == null) {
            return List.of();
        }

        return staff.stream()
                .map(s -> CourseResponseDto.TeachingStaffDto.builder()
                        .userId(s.getUserId())
                        .role(s.getRole())
                        .build())
                .toList();
    }

    private static List<Course.TeachingStaff> dtoStaffToModel(List<CourseRequestDto.TeachingStaffDto> staffDto) {

        if(staffDto == null){
            return List.of();
        }

        return staffDto.stream()
                .map(dto -> Course.TeachingStaff.builder()
                        .userId(dto.getUserId())
                        .role(dto.getRole())
                        .build())
                .toList();
    }
}
