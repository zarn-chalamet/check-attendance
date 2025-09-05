package com.ai.attendance_check.attendance_service.repository;

import com.ai.attendance_check.attendance_service.model.AttendanceSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttendanceSessionRepository extends MongoRepository<AttendanceSession,String> {

    List<AttendanceSession> findAllByCourseId(String courseId);
}