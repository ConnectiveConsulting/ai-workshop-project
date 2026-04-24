package com.example.trainingmanagement.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for registrations.
 * Nested under /api/courses/{id}/registrations for listing and creating.
 * Delete uses /api/registrations/{id} for simplicity.
 */
@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /** List all registrations for a course (admin). */
    @GetMapping("/api/courses/{courseId}/registrations")
    public List<Registration> getRegistrations(@PathVariable Long courseId) {
        return registrationService.findByCourseId(courseId);
    }

    /**
     * Register for a course (public).
     * Request body: { "name": "...", "email": "..." }
     */
    @PostMapping("/api/courses/{courseId}/registrations")
    public ResponseEntity<Registration> register(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> body) {

        String name = body.get("name");
        String email = body.get("email");
        Registration registration = registrationService.register(courseId, name, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }

    /** Cancel a registration (admin or self). */
    @DeleteMapping("/api/registrations/{id}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long id) {
        if (!registrationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        registrationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
