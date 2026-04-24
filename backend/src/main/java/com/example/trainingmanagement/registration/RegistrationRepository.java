package com.example.trainingmanagement.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Spring Data repository for Registration entities.
 */
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /** Find all registrations for a given course. */
    List<Registration> findByCourse_Id(Long courseId);

    /** Count how many students are registered for a course (for capacity checks). */
    long countByCourse_Id(Long courseId);
}
