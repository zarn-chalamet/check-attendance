package com.ai.attendance_check.course_service.repository;

import com.ai.attendance_check.course_service.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course,String> {
    boolean existsByCode(String code);
}
