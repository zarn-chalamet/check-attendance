package com.ai.attendance_check.course_service.service;

import com.ai.attendance_check.course_service.dto.request.CourseRequestDto;
import com.ai.attendance_check.course_service.dto.response.CourseResponseDto;
import com.ai.attendance_check.course_service.user.UserResponseDto;

import java.util.List;

public interface CourseService {
    CourseResponseDto createNewCourse(CourseRequestDto courseDto);

    List<CourseResponseDto> getAllCourses();

    List<CourseResponseDto> getAllCoursesByKeycloakId(String keycloakId);

    CourseResponseDto getCourseByCourseId(String courseId);

    CourseResponseDto updateCourseByCourseId(String courseId, CourseRequestDto courseDto);

    void deleteCourseByCourseId(String courseId);

    List<UserResponseDto> getStudentListByCourseId(String courseId);

    List<UserResponseDto> getStaffListByCourseId(String courseId);

    CourseResponseDto addStudentToCourse(String courseId, String studentId);

    CourseResponseDto removeStudentFromCourse(String courseId, String studentId);

    CourseResponseDto addTeachingStaffToCourse(String courseId, String staffId, String role);

    CourseResponseDto updateTeachingStaffRole(String courseId, String staffId, String role);

    CourseResponseDto toggleCourseActive(String courseId);
}
