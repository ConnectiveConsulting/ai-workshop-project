package com.example.trainingmanagement.course;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for courses.
 * Kept intentionally thin — delegates to the repository.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }
}
