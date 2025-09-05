package com.ai.attendance_check.course_service.service.impl;

import com.ai.attendance_check.course_service.dto.request.CourseRequestDto;
import com.ai.attendance_check.course_service.dto.response.CourseResponseDto;
import com.ai.attendance_check.course_service.exception.CourseAlreadyRegisteredException;
import com.ai.attendance_check.course_service.exception.CourseNotFoundException;
import com.ai.attendance_check.course_service.mapper.CourseMapper;
import com.ai.attendance_check.course_service.model.Course;
import com.ai.attendance_check.course_service.repository.CourseRepository;
import com.ai.attendance_check.course_service.service.CourseService;
import com.ai.attendance_check.course_service.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final WebClient webClient;

    @Override
    public CourseResponseDto createNewCourse(CourseRequestDto courseDto) {

        boolean courseExist = courseRepository.existsByCode(courseDto.getCode());
        if(courseExist) {
            throw new CourseAlreadyRegisteredException("Course already registered with code: "+ courseDto.getCode());
        }

        Course course = CourseMapper.dtoToModel(courseDto);
        Course createdCourse = courseRepository.save(course);

        return CourseMapper.mapToDto(createdCourse);
    }

    @Override
    public List<CourseResponseDto> getAllCourses() {

        List<Course> allCourses = courseRepository.findAll();

        return allCourses.stream()
                .map(CourseMapper::mapToDto)
                .toList();
    }

    @Override
    public List<CourseResponseDto> getAllCoursesByKeycloakId(String keycloakId) {

        List<Course> allCourses = courseRepository.findAll();

        List<Course> coursesByUser = allCourses.stream().filter(
                course -> (course.getStudentIds() != null && course.getStudentIds().contains(keycloakId)) ||
                        (course.getStaff() != null && course.getStaff().stream()
                                .anyMatch(staff -> staff.getUserId().equals(keycloakId)))
        ).toList();

        System.out.println("====================================");
        System.out.println(coursesByUser);

        return coursesByUser.stream()
                .map(CourseMapper::mapToDto)
                .toList();
    }

    @Override
    public CourseResponseDto getCourseByCourseId(String courseId) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        return CourseMapper.mapToDto(course);
    }

    @Override
    public CourseResponseDto updateCourseByCourseId(String courseId, CourseRequestDto courseDto) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        // Update fields
        if (courseDto.getCode() != null) course.setCode(courseDto.getCode());
        if (courseDto.getTitle() != null) course.setTitle(courseDto.getTitle());
        if (courseDto.getDescription() != null) course.setDescription(courseDto.getDescription());
        if (courseDto.getStartDate() != null) course.setStartDate(courseDto.getStartDate());
        if (courseDto.getEndDate() != null) course.setEndDate(courseDto.getEndDate());
        if (courseDto.isActive()) course.setActive(true);

        //Update staff or students
        if (courseDto.getStaff() != null) course.setStaff(courseDto.getStaff().stream()
                .map( s -> Course.TeachingStaff.builder()
                        .userId(s.getUserId())
                        .role(s.getRole())
                        .build()
        ).toList());
        if (courseDto.getStudentIds() != null) course.setStudentIds(courseDto.getStudentIds());

        // Save updated course
        Course updatedCourse = courseRepository.save(course);

        return CourseMapper.mapToDto(updatedCourse);
    }

    @Override
    public void deleteCourseByCourseId(String courseId) {

        boolean courseExist = courseRepository.existsById(courseId);
        if(!courseExist){
            throw new CourseNotFoundException("Course not found with id: "+ courseId);
        }

        courseRepository.deleteById(courseId);
    }

    @Override
    public List<UserResponseDto> getStudentListByCourseId(String courseId) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        //get user information from user-service(getUserById)
        List<String> studentIds = course.getStudentIds();

        // Fetch users from user-service using WebClient
        return studentIds.stream()
                .map(this::getUserById)
                .toList();
    }

    @Override
    public List<UserResponseDto> getStaffListByCourseId(String courseId) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        List<Course.TeachingStaff> staffList = course.getStaff();

        return staffList.stream()
                .map(staff -> getUserById(staff.getUserId()))
                .toList();
    }

    private UserResponseDto getUserById(String keycloakId) {
        return webClient.get()
                .uri("/api/users/{id}", keycloakId)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block(); // blocking here for simplicity; optional: use reactive stream fully
    }

    @Override
    public CourseResponseDto addStudentToCourse(String courseId, String studentId) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        // Initialize student list if null
        if (course.getStudentIds() == null) {
            course.setStudentIds(new ArrayList<>());
        }

        // Add studentId if not already present
        if (!course.getStudentIds().contains(studentId)) {
            course.getStudentIds().add(studentId);
        }

        // Save updated course
        Course updatedCourse = courseRepository.save(course);

        return CourseMapper.mapToDto(updatedCourse);
    }

    @Override
    public CourseResponseDto removeStudentFromCourse(String courseId, String studentId) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        // If student list is null, just return DTO
        if (course.getStudentIds() != null) {
            course.getStudentIds().remove(studentId);
            courseRepository.save(course);
        }
        return CourseMapper.mapToDto(course);
    }

    @Override
    public CourseResponseDto addTeachingStaffToCourse(String courseId,
                                                      String staffId,
                                                      String role) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        // Initialize staff list if null
        if (course.getStaff() == null) {
            course.setStaff(new ArrayList<>());
        }

        // Check if staff already exists
        boolean alreadyExists = course.getStaff().stream()
                .anyMatch(s -> s.getUserId().equals(staffId));


        if (!alreadyExists) {
            // Add new staff
            course.getStaff().add(
                    Course.TeachingStaff.builder()
                            .userId(staffId)
                            .role(role)
                            .build()
            );
        }

        // Save updated course
        Course updatedCourse = courseRepository.save(course);

        return CourseMapper.mapToDto(updatedCourse);
    }

    @Override
    public CourseResponseDto updateTeachingStaffRole(String courseId,
                                                     String staffId,
                                                     String role) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "+courseId));

        // Update role if staff exists
        boolean updated = false;
        if (course.getStaff() != null) {
            for (Course.TeachingStaff s : course.getStaff()) {
                if (s.getUserId().equals(staffId)) {
                    s.setRole(role);
                    updated = true;
                    break;
                }
            }
        }

        if (!updated) {
            throw new RuntimeException("Staff not found with id: " + staffId);
        }

        // Save updated course
        Course updatedCourse = courseRepository.save(course);

        return CourseMapper.mapToDto(updatedCourse);
    }

    @Override
    public CourseResponseDto toggleCourseActive(String courseId) {

        // Find existing course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        // Toggle active status
        course.setActive(!course.isActive());

        // Save updated course
        Course updatedCourse = courseRepository.save(course);

        return CourseMapper.mapToDto(updatedCourse);
    }
}
