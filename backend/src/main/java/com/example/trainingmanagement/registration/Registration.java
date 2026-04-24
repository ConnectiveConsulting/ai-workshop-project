package com.example.trainingmanagement.registration;

import com.example.trainingmanagement.course.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a student's registration for a course.
 */
@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private LocalDateTime registeredAt;

    // --- Constructors ---

    public Registration() {}

    public Registration(Course course, String name, String email, LocalDateTime registeredAt) {
        this.course = course;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /** Expose the course ID in JSON without nesting the full course object. */
    public Long getCourseId() {
        return course != null ? course.getId() : null;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}
