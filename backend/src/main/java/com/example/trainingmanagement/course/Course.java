package com.example.trainingmanagement.course;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import java.time.LocalDate;

/**
 * Represents a training course offered in the system.
 */
@Entity
@Table(name = "courses")
@Check(
    name = "chk_courses_length_and_capacity",
    constraints = "length(title) <= 200 and length(description) <= 2000 and " +
            "(instructor is null or length(instructor) <= 255) and " +
            "(duration is null or length(duration) <= 255) and " +
            "(category is null or length(category) <= 255) and " +
            "(content is null or length(content) <= 10000) and " +
            "(capacity is null or capacity >= 1)"
)
public class Course {

    public static final int TITLE_MAX_LENGTH = 200;
    public static final int DESCRIPTION_MAX_LENGTH = 2000;
    public static final int INSTRUCTOR_MAX_LENGTH = 255;
    public static final int DURATION_MAX_LENGTH = 255;
    public static final int CATEGORY_MAX_LENGTH = 255;
    public static final int CONTENT_MAX_LENGTH = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(nullable = false, length = DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(length = INSTRUCTOR_MAX_LENGTH)
    private String instructor;

    private LocalDate date;

    @Column(length = DURATION_MAX_LENGTH)
    private String duration;

    private Integer capacity;

    @Column(length = CATEGORY_MAX_LENGTH)
    private String category;

    @Column(columnDefinition = "TEXT", length = CONTENT_MAX_LENGTH)
    private String content;

    // --- Constructors ---

    public Course() {}

    public Course(String title, String description, String instructor, LocalDate date,
                  String duration, Integer capacity, String category, String content) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.date = date;
        this.duration = duration;
        this.capacity = capacity;
        this.category = category;
        this.content = content;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
