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
    public ResponseEntity<?> register(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> body) {

        String validationMessage = validateRegistrationPayload(body);
        if (validationMessage != null) {
            return badRequest(validationMessage);
        }

        String name = body.get("name");
        String email = body.get("email");
        String phone = body.get("phone");

        Registration registration = registrationService.register(courseId, name, email, phone);
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

    private String validateRegistrationPayload(Map<String, String> body) {
        if (body == null) {
            return "Registration payload is required";
        }

        String name = body.get("name");
        String email = body.get("email");
        String phone = body.get("phone");

        String message = requireField("name", name);
        if (message != null) {
            return message;
        }
        message = requireField("email", email);
        if (message != null) {
            return message;
        }
        message = requireField("phone", phone);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("name", name, Registration.NAME_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validateMaxLength("phone", phone, Registration.PHONE_MAX_LENGTH);
        if (message != null) {
            return message;
        }
        message = validatePhoneCharacters(phone);
        if (message != null) {
            return message;
        }

        return null;
    }

    private String validatePhoneCharacters(String phone) {
        if (phone != null && !phone.matches("[0-9 +\\-()]+")) {
            return "Field 'phone' contains invalid characters. Use digits, spaces, +, -, or parentheses only.";
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
