package com.example.trainingmanagement.registration;

import com.example.trainingmanagement.course.Course;
import com.example.trainingmanagement.course.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for registrations.
 * Enforces the capacity limit before allowing a registration.
 */
@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
    }

    /** Return all registrations for a course. */
    public List<Registration> findByCourseId(Long courseId) {
        return registrationRepository.findByCourse_Id(courseId);
    }

    /** Count registrations for a course (used to compute seats remaining). */
    public long countByCourseId(Long courseId) {
        return registrationRepository.countByCourse_Id(courseId);
    }

    /**
     * Register a student for a course.
     * Throws 404 if the course doesn't exist.
     * Throws 409 if the course is already at capacity.
     */
    public Registration register(Long courseId, String name, String email) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        long registered = registrationRepository.countByCourse_Id(courseId);
        if (course.getCapacity() != null && registered >= course.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course is full");
        }

        Registration registration = new Registration(course, name, email, LocalDateTime.now());
        return registrationRepository.save(registration);
    }

    /** Find a registration by ID. */
    public Optional<Registration> findById(Long id) {
        return registrationRepository.findById(id);
    }

    /** Cancel (delete) a registration. */
    public void deleteById(Long id) {
        registrationRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registrationRepository.existsById(id);
    }
}
