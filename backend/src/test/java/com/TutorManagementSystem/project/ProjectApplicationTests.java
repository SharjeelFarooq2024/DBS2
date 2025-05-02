package com.TutorManagementSystem.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.TutorManagementSystem.TutorManagementSystemApplication;

@SpringBootTest(classes = TutorManagementSystemApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class ProjectApplicationTests {
    @Test
    public void contextLoads() {
        // This test verifies that the Spring context loads successfully
    }
}
