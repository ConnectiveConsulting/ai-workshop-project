package com.example.trainingmanagement.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for course CRUD operations.
 * Public endpoints: GET (list and detail).
 * Admin endpoints: POST, PUT, DELETE.
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /** List all courses. */
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    /** Get a single course by ID. */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Create a new course. */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course saved = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Update an existing course. */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updates) {
        return courseService.findById(id).map(existing -> {
            existing.setTitle(updates.getTitle());
            existing.setDescription(updates.getDescription());
            existing.setInstructor(updates.getInstructor());
            existing.setDate(updates.getDate());
            existing.setDuration(updates.getDuration());
            existing.setCapacity(updates.getCapacity());
            existing.setCategory(updates.getCategory());
            existing.setContent(updates.getContent());
            return ResponseEntity.ok(courseService.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Delete a course by ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
