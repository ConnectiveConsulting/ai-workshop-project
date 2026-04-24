package com.example.trainingmanagement.seed;

import com.example.trainingmanagement.course.Course;
import com.example.trainingmanagement.course.CourseRepository;
import com.example.trainingmanagement.registration.Registration;
import com.example.trainingmanagement.registration.RegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Development-only endpoint to populate the database with sample data.
 * Call POST /api/seed to reset and re-seed the database.
 */
@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;

    public SeedController(CourseRepository courseRepository,
                          RegistrationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> seed() {
        // Clear existing data
        registrationRepository.deleteAll();
        courseRepository.deleteAll();

        // Create sample courses
        List<Course> courses = courseRepository.saveAll(List.of(
            new Course(
                "Intro to Java",
                "A beginner-friendly introduction to the Java programming language. " +
                "Covers variables, control flow, object-oriented principles, and the standard library.",
                "Alice Johnson",
                LocalDate.of(2026, 5, 10),
                "2 days",
                20,
                "Development",
                """
                ## Course Overview
                This course introduces the fundamentals of Java programming.

                ## Topics Covered
                - Java syntax and data types
                - Classes and objects
                - Inheritance and interfaces
                - Exception handling
                - Collections framework

                ## Prerequisites
                No prior programming experience required.
                """
            ),
            new Course(
                "Spring Boot Fundamentals",
                "Learn how to build production-ready REST APIs using Spring Boot. " +
                "Covers auto-configuration, dependency injection, data access, and testing.",
                "Bob Martinez",
                LocalDate.of(2026, 5, 20),
                "3 days",
                15,
                "Development",
                """
                ## Course Overview
                Build modern Java web applications with Spring Boot.

                ## Topics Covered
                - Spring Boot auto-configuration
                - REST controllers and request mapping
                - Spring Data JPA and repositories
                - Application properties and profiles
                - Unit and integration testing

                ## Prerequisites
                Java experience recommended.
                """
            ),
            new Course(
                "Docker & Containers",
                "Understand containerization from the ground up. Learn to build Docker images, " +
                "run containers, and manage multi-container apps with Docker Compose.",
                "Carol Smith",
                LocalDate.of(2026, 6, 3),
                "1 day",
                25,
                "DevOps",
                """
                ## Course Overview
                Get hands-on with Docker and container-based deployments.

                ## Topics Covered
                - What is containerization and why it matters
                - Writing Dockerfiles
                - Building and tagging images
                - Running and inspecting containers
                - Docker Compose for local development

                ## Prerequisites
                Basic command-line familiarity.
                """
            ),
            new Course(
                "CI/CD Pipelines",
                "Design and implement continuous integration and delivery pipelines. " +
                "Covers GitHub Actions, automated testing, and deployment strategies.",
                "David Lee",
                LocalDate.of(2026, 6, 17),
                "2 days",
                20,
                "DevOps",
                """
                ## Course Overview
                Automate your build, test, and deploy workflow.

                ## Topics Covered
                - CI/CD principles and pipeline stages
                - GitHub Actions workflows
                - Running automated tests in CI
                - Deployment strategies (blue/green, rolling)
                - Secrets and environment management

                ## Prerequisites
                Git and basic DevOps familiarity.
                """
            ),
            new Course(
                "AWS for Developers",
                "Practical introduction to AWS services for application developers. " +
                "Covers EC2, S3, RDS, Lambda, and IAM with hands-on labs.",
                "Emma Wilson",
                LocalDate.of(2026, 7, 8),
                "2 days",
                18,
                "Cloud",
                """
                ## Course Overview
                Build and deploy applications on Amazon Web Services.

                ## Topics Covered
                - AWS core services overview
                - EC2 instances and security groups
                - S3 for object storage
                - RDS for managed databases
                - Lambda serverless functions
                - IAM roles and policies

                ## Prerequisites
                Some web development experience helpful.
                """
            ),
            new Course(
                "Kubernetes in Practice",
                "Go beyond Docker and manage containerized applications at scale with Kubernetes. " +
                "Deploy, scale, and operate services in a real cluster.",
                "Frank Chen",
                LocalDate.of(2026, 7, 22),
                "3 days",
                12,
                "Cloud",
                """
                ## Course Overview
                Master container orchestration with Kubernetes.

                ## Topics Covered
                - Kubernetes architecture and concepts
                - Pods, Deployments, and Services
                - ConfigMaps and Secrets
                - Persistent storage
                - Horizontal pod autoscaling
                - Helm charts

                ## Prerequisites
                Docker experience required.
                """
            ),
            new Course(
                "Engineering Leadership 101",
                "Transition from individual contributor to engineering lead. " +
                "Covers giving feedback, running effective meetings, and growing a team.",
                "Grace Park",
                LocalDate.of(2026, 8, 5),
                "1 day",
                30,
                "Leadership",
                """
                ## Course Overview
                Develop the skills to lead engineering teams effectively.

                ## Topics Covered
                - The IC-to-manager transition
                - Giving and receiving feedback
                - Running 1:1s and team meetings
                - Hiring and onboarding
                - Technical roadmap planning

                ## Prerequisites
                No prerequisites — open to all engineers.
                """
            ),
            new Course(
                "AI-Assisted Development",
                "Learn how to use AI coding assistants effectively in your daily development workflow. " +
                "Covers prompt engineering, code review with AI, and responsible use.",
                "Henry Brown",
                LocalDate.of(2026, 8, 19),
                "1 day",
                35,
                "Development",
                """
                ## Course Overview
                Supercharge your productivity with AI-powered development tools.

                ## Topics Covered
                - Overview of AI coding assistants
                - Writing effective prompts for code generation
                - Reviewing and validating AI-generated code
                - AI-assisted debugging and refactoring
                - Responsible and ethical use of AI tools

                ## Prerequisites
                Any programming experience welcome.
                """
            )
        ));

        // Create sample registrations
        registrationRepository.saveAll(List.of(
            new Registration(courses.get(0), "Sarah Connor", "sarah.connor@example.com", LocalDateTime.now().minusDays(10)),
            new Registration(courses.get(0), "John Reese", "j.reese@example.com", LocalDateTime.now().minusDays(9)),
            new Registration(courses.get(0), "Amy Pond", "amy.pond@example.com", LocalDateTime.now().minusDays(8)),
            new Registration(courses.get(1), "Bruce Wayne", "bruce@wayneenterprises.com", LocalDateTime.now().minusDays(7)),
            new Registration(courses.get(1), "Diana Prince", "diana.prince@example.com", LocalDateTime.now().minusDays(6)),
            new Registration(courses.get(2), "Peter Parker", "p.parker@dailybugle.com", LocalDateTime.now().minusDays(5)),
            new Registration(courses.get(2), "Mary Watson", "mwatson@example.com", LocalDateTime.now().minusDays(5)),
            new Registration(courses.get(3), "Tony Stark", "tony@starkindustries.com", LocalDateTime.now().minusDays(4)),
            new Registration(courses.get(4), "Natasha Romanoff", "n.romanoff@example.com", LocalDateTime.now().minusDays(3)),
            new Registration(courses.get(4), "Steve Rogers", "steve.rogers@example.com", LocalDateTime.now().minusDays(3)),
            new Registration(courses.get(5), "Lena Luthor", "lena@lcorp.com", LocalDateTime.now().minusDays(2)),
            new Registration(courses.get(6), "James Rhodes", "j.rhodes@example.com", LocalDateTime.now().minusDays(1)),
            new Registration(courses.get(7), "Sam Wilson", "sam.wilson@example.com", LocalDateTime.now()),
            new Registration(courses.get(7), "Wanda Maximoff", "wanda@example.com", LocalDateTime.now()),
            new Registration(courses.get(7), "Vision Android", "vision@example.com", LocalDateTime.now())
        ));

        return ResponseEntity.ok(Map.of(
            "message", "Database seeded successfully",
            "courses", courses.size(),
            "registrations", 15
        ));
    }
}
