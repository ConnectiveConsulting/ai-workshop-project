package com.example.trainingmanagement.registration;

import com.example.trainingmanagement.course.Course;
import com.example.trainingmanagement.course.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class RegistrationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    private MockMvc mockMvc;

    private Long courseId;
    private Long fullCourseId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        registrationRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();

        Course course = new Course("Test Course", "A test course description",
                "Instructor", null, "1 day", null, "Testing", null);
        courseId = courseRepository.save(course).getId();

        Course fullCourse = new Course("Full Course", "A full course description",
                "Instructor", null, "1 day", 1, "Testing", null);
        fullCourse = courseRepository.save(fullCourse);
        fullCourseId = fullCourse.getId();

        Registration reg = new Registration(
                fullCourse,
                "Existing User", "existing@example.com", "+1 555 000 0000", LocalDateTime.now());
        registrationRepository.save(reg);
    }

    @Test
    void register_missingPhone_returnsBadRequest() throws Exception {
        String body = "{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}";

        mockMvc.perform(post("/api/courses/{courseId}/registrations", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Field 'phone' is required"));
    }

    @Test
    void register_invalidPhoneCharacters_returnsBadRequest() throws Exception {
        String body = "{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\",\"phone\":\"abc123\"}";

        mockMvc.perform(post("/api/courses/{courseId}/registrations", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Field 'phone' contains invalid characters. Use digits, spaces, +, -, or parentheses only."));
    }

    @Test
    void register_validPayload_returnsCreatedWithPhone() throws Exception {
        String body = "{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\",\"phone\":\"+1 (555) 123-4567\"}";

        mockMvc.perform(post("/api/courses/{courseId}/registrations", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.phone").value("+1 (555) 123-4567"));
    }

    @Test
    void register_fullCourse_returnsConflict() throws Exception {
        String body = "{\"name\":\"Late Registrant\",\"email\":\"late@example.com\",\"phone\":\"+1 555 999 9999\"}";

        mockMvc.perform(post("/api/courses/{courseId}/registrations", fullCourseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }
}
