package com.example.trainingmanagement.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        String validationMessage = validateCourse(course);
        if (validationMessage != null) {
            return badRequest(validationMessage);
        }

        Course saved = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Update an existing course. */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course updates) {
        return courseService.findById(id).map(existing -> {
            String validationMessage = validateCourse(updates);
            if (validationMessage != null) {
                return badRequest(validationMessage);
            }

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

    private String validateCourse(Course course) {
        if (course == null) {
            return "Course payload is required";
        }

        String message = requireField("title", course.getTitle());
        if (message != null) {
            return message;
        }
        message = requireField("description", course.getDescription());
        if (message != null) {
            return message;
        }

        message = validateMaxLength("title", course.getTitle(), Course.TITLE_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("description", course.getDescription(), Course.DESCRIPTION_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("instructor", course.getInstructor(), Course.INSTRUCTOR_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("duration", course.getDuration(), Course.DURATION_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("category", course.getCategory(), Course.CATEGORY_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("content", course.getContent(), Course.CONTENT_MAX_LENGTH);
        if (message != null) {
            return message;
        }

        Integer capacity = course.getCapacity();
        if (capacity != null && capacity < 1) {
            return "Field 'capacity' must be greater than or equal to 1";
        }

        return null;
    }

    private String requireField(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            return "Field '" + fieldName + "' is required";
        }

        return null;
    }

    private String validateMaxLength(String fieldName, String value, int maxLength) {
        if (value != null && value.length() > maxLength) {
            return "Field '" + fieldName + "' must be at most " + maxLength + " characters";
        }

        return null;
    }

    private ResponseEntity<Map<String, String>> badRequest(String message) {
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}
