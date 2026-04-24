package com.example.trainingmanagement.course;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for Course entities.
 * Provides standard CRUD operations with no extra code needed.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
}
