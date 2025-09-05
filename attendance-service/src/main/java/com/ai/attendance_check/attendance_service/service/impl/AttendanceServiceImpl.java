package com.ai.attendance_check.attendance_service.service.impl;

import com.ai.attendance_check.attendance_service.course.CourseResponseDto;
import com.ai.attendance_check.attendance_service.dto.CourseAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.StudentAttendanceReportDto;
import com.ai.attendance_check.attendance_service.dto.request.AttendanceRequestDto;
import com.ai.attendance_check.attendance_service.dto.response.AttendanceResponseDto;
import com.ai.attendance_check.attendance_service.exception.AttendanceSessionNotFoundException;
import com.ai.attendance_check.attendance_service.exception.SessionAlreadyClosedException;
import com.ai.attendance_check.attendance_service.exception.StudentAlreadyCheckInException;
import com.ai.attendance_check.attendance_service.mapper.AttendanceMapper;
import com.ai.attendance_check.attendance_service.model.AttendanceSession;
import com.ai.attendance_check.attendance_service.repository.AttendanceSessionRepository;
import com.ai.attendance_check.attendance_service.service.AttendanceService;
import com.ai.attendance_check.attendance_service.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final int CUTOFF_MINUTES = 30;

    private final AttendanceSessionRepository attendanceSessionRepository;

    private final WebClient userWebClient;

    private final WebClient courseWebClient;

    public AttendanceServiceImpl(AttendanceSessionRepository attendanceSessionRepository,
                                 @Qualifier("userServiceWebClient") WebClient userWebClient,
                                 @Qualifier("courseServiceWebClient") WebClient courseWebClient) {
        this.attendanceSessionRepository = attendanceSessionRepository;
        this.userWebClient = userWebClient;
        this.courseWebClient = courseWebClient;
    }

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
        // get session by id
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AttendanceSessionNotFoundException(
                        "Attendance session not found with id: " + sessionId));

        // check session active
        if (!session.isActive()) {
            throw new RuntimeException("Session already closed");
        }

        // check already checked in
        boolean isAlreadyCheckIn = session.getRecords() != null &&
                session.getRecords().stream().anyMatch(r -> r.getStudentId().equals(studentId));

        if (isAlreadyCheckIn) {
            throw new StudentAlreadyCheckInException(
                    "Student with this id already checked in: " + studentId);
        }

        // session logic
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(session.getEndTime())) {
            throw new RuntimeException("Session has already ended. Cannot check in.");
        }

        boolean present = now.isBefore(session.getStartTime().plusMinutes(CUTOFF_MINUTES));
        boolean late = !present && now.isBefore(session.getEndTime());

        // initialize record list if null
        if (session.getRecords() == null) {
            session.setRecords(new ArrayList<>());
        }

        // set attendance record
        AttendanceSession.AttendanceRecord record = AttendanceSession.AttendanceRecord
                .builder()
                .studentId(studentId)
                .checkIn(now)
                .present(present)
                .late(late)
                .build();

        session.getRecords().add(record);

        // save to repository
        AttendanceSession updatedSession = attendanceSessionRepository.save(session);

        return AttendanceMapper.mapToDto(updatedSession);
    }

    @Override
    public AttendanceResponseDto closeSession(String sessionId) {

        //get session by id
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AttendanceSessionNotFoundException("Attendance section not found with id: "+ sessionId));

        //check session is already closed or not
        if(!session.isActive()){
            throw new SessionAlreadyClosedException("Session is already closed.");
        }
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

        // Get user data from user-service
        UserResponseDto user = getUser(studentId);

        // Get course data from course-service
        CourseResponseDto course = getCourse(courseId);

        // Get all attendance sessions for the course
        List<AttendanceResponseDto> sessions = getAllAttendanceSessionByCourseId(courseId);

        int totalSessions = sessions.size();

        // Count how many sessions the student attended
        int attendedSessions = (int) sessions.stream()
                .flatMap(session -> session.getRecords().stream())
                .filter(record -> record.getStudentId().equals(studentId))
                .count();

        int missedSessions = totalSessions - attendedSessions;

        // Map session records for the student
        List<StudentAttendanceReportDto.SessionRecord> sessionRecords = sessions.stream()
                .map(session -> {
                    boolean attended = session.getRecords().stream()
                            .anyMatch(r -> r.getStudentId().equals(studentId));
                    String status = attended ? "Present" : "Absent";

                    return StudentAttendanceReportDto.SessionRecord.builder()
                            .sessionId(session.getId())
                            .date(session.getStartTime()) // or session.getCreatedAt()
                            .attended(attended)
                            .status(status)
                            .build();
                })
                .toList();

        // Build report
        return StudentAttendanceReportDto.builder()
                .courseId(courseId)
                .studentId(studentId)
                .studentName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .totalSessions(totalSessions)
                .attendedSessions(attendedSessions)
                .missedSessions(missedSessions)
                .attendanceRate(totalSessions == 0 ? 0 : (attendedSessions * 100.0 / totalSessions))
                .sessionRecords(sessionRecords)
                .build();
    }

    @Override
    public CourseAttendanceReportDto getCourseReport(String courseId) {

        // Get course data from course-service
        CourseResponseDto course = getCourse(courseId);

        // Get all attendance sessions for the course
        List<AttendanceResponseDto> sessions = getAllAttendanceSessionByCourseId(courseId);

        int totalSessions = sessions.size();
        int totalStudents = course.getStudentIds().size();

        // total number of "present marks" across all sessions
        long totalAttendances = sessions.stream()
                .mapToLong(session -> session.getRecords().size())
                .sum();

        // maximum possible attendances = total sessions * total students
        int maxPossibleAttendances = totalSessions * totalStudents;

        // average attendance rate (percentage)
        double averageAttendanceRate = maxPossibleAttendances == 0 ? 0.0 :
                (totalAttendances * 100.0) / maxPossibleAttendances;

        // 6. Per-student breakdown
        List<CourseAttendanceReportDto.StudentSummary> studentSummaries = course.getStudentIds()
                .stream()
                .map(studentId -> {
                    // Count sessions attended by this student
                    int attendedSessions = (int) sessions.stream()
                            .flatMap(session -> session.getRecords().stream())
                            .filter(record -> record.getStudentId().equals(studentId))
                            .count();

                    int missedSessions = totalSessions - attendedSessions;

                    // Fetch student info (name)
                    UserResponseDto user = getUser(studentId);

                    double attendanceRate = totalSessions == 0 ? 0.0 :
                            (attendedSessions * 100.0) / totalSessions;

                    return CourseAttendanceReportDto.StudentSummary.builder()
                            .studentId(studentId)
                            .studentName(user.getFirstName() + " " + user.getLastName())
                            .attendedSessions(attendedSessions)
                            .missedSessions(missedSessions)
                            .attendanceRate(attendanceRate)
                            .build();
                })
                .toList();

        return CourseAttendanceReportDto.builder()
                .courseId(courseId)
                .courseCode(course.getCode())
                .courseTitle(course.getTitle())
                .totalSessions(totalSessions)
                .totalStudents(totalStudents)
                .averageAttendanceRate(averageAttendanceRate)
                .studentSummaries(studentSummaries)
                .build();
    }

    @Override
    public void deleteSession(String sessionId) {

        boolean existSession = attendanceSessionRepository.existsById(sessionId);

        if(existSession) {
            attendanceSessionRepository.deleteById(sessionId);
        }
    }

    public UserResponseDto getUser(String keycloakId) {
        return userWebClient.get()
                .uri("/api/users/{id}", keycloakId)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block(); // or use reactive flow
    }

    public CourseResponseDto getCourse(String courseId) {
        return courseWebClient.get()
                .uri("/api/courses/{id}", courseId)
                .retrieve()
                .bodyToMono(CourseResponseDto.class)
                .block();
    }
}
