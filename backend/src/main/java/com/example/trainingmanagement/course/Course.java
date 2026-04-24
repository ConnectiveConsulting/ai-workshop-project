package com.example.trainingmanagement.course;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Represents a training course offered in the system.
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    private String instructor;

    private LocalDate date;

    private String duration;

    private Integer capacity;

    private String category;

    @Column(columnDefinition = "TEXT")
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
