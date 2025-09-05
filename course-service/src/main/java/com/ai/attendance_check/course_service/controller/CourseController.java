package com.ai.attendance_check.course_service.controller;

import com.ai.attendance_check.course_service.dto.request.CourseRequestDto;
import com.ai.attendance_check.course_service.dto.response.CourseResponseDto;
import com.ai.attendance_check.course_service.service.CourseService;
import com.ai.attendance_check.course_service.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    //create course (only admin)
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto courseDto) {
        CourseResponseDto course = courseService.createNewCourse(courseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    //get all courses (only admin)
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getCourses() {
        List<CourseResponseDto> courses = courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    //get courses by user(lecturer,student)
    @GetMapping("/user/{keycloakId}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByUser(@PathVariable String keycloakId) {
        List<CourseResponseDto> courses = courseService.getAllCoursesByKeycloakId(keycloakId);

        return ResponseEntity.ok(courses);
    }

    //get course by id
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable String courseId) {
        CourseResponseDto course = courseService.getCourseByCourseId(courseId);

        return ResponseEntity.ok(course);
    }

    //update course (only admin)
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> updateCourseById(@PathVariable String courseId,
                                                              @RequestBody CourseRequestDto courseDto) {
        CourseResponseDto course = courseService.updateCourseByCourseId(courseId,courseDto);

        return ResponseEntity.ok(course);
    }

    //delete course
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourseById(@PathVariable String courseId) {
        courseService.deleteCourseByCourseId(courseId);

        return ResponseEntity.ok("Deleted Course successfully");
    }

    //get student list by course id
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<UserResponseDto>> getStudentsByCourseId(@PathVariable String courseId) {
        List<UserResponseDto> students = courseService.getStudentListByCourseId(courseId);

        return ResponseEntity.ok(students);
    }

    //get teaching staff list by course id
    @GetMapping("/{courseId}/staff")
    public ResponseEntity<List<UserResponseDto>> getStaffByCourseId(@PathVariable String courseId) {
        List<UserResponseDto> staff = courseService.getStaffListByCourseId(courseId);

        return ResponseEntity.ok(staff);
    }

    //add student to course by studentId(keycloakId)
    @PostMapping("/{courseId}/students")
    public ResponseEntity<CourseResponseDto> addStudentToCourseByAdmin(@PathVariable String courseId,
                                                                       @RequestParam("studentId") String studentId) {
        CourseResponseDto updatedCourse = courseService.addStudentToCourse(courseId,studentId);

        return ResponseEntity.ok(updatedCourse);
    }

    //remove student from course
    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<CourseResponseDto> removeStudentFromCourseByAdmin(@PathVariable("courseId") String courseId,
                                                                       @PathVariable("studentId") String studentId) {
        CourseResponseDto updatedCourse = courseService.removeStudentFromCourse(courseId,studentId);

        return ResponseEntity.ok(updatedCourse);
    }

    //add teaching staff
    @PostMapping("/{courseId}/staff")
    public ResponseEntity<CourseResponseDto> addStaffToCourse(@PathVariable String courseId,
                                                              @RequestParam("staffId") String staffId,
                                                              @RequestParam("role") String role) {
        CourseResponseDto updatedCourse = courseService.addTeachingStaffToCourse(courseId,staffId,role);

        return ResponseEntity.ok(updatedCourse);
    }

    //update staff role
    @PatchMapping("/{courseId}/staff/{staffId}")
    public ResponseEntity<CourseResponseDto> updateStaffRole(@PathVariable String courseId,
                                                              @PathVariable("staffId") String staffId,
                                                              @RequestParam("role") String role) {
        CourseResponseDto updatedCourse = courseService.updateTeachingStaffRole(courseId,staffId,role);

        return ResponseEntity.ok(updatedCourse);
    }

    //deactivate/activate course
    @PatchMapping("/{courseId}/toggle-active")
    public ResponseEntity<CourseResponseDto> toggleActiveCourse(@PathVariable String courseId) {
        CourseResponseDto updatedCourse = courseService.toggleCourseActive(courseId);

        return ResponseEntity.ok(updatedCourse);
    }
}
