package com.ai.attendance_check.attendance_service.repository;

import com.ai.attendance_check.attendance_service.model.AttendanceSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttendanceSessionRepository extends MongoRepository<AttendanceSession,String> {

}
