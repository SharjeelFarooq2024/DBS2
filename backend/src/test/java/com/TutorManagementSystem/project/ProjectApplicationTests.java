package com.TutorManagementSystem.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.TutorManagementSystem.TutorManagementSystemApplication;

@SpringBootTest(classes = TutorManagementSystemApplication.class)
@ActiveProfiles("test") // Use test profile instead of directly referencing properties
public class ProjectApplicationTests {
    @Test
    public void contextLoads() {
        // This test verifies that the Spring context loads successfully
    }
}
