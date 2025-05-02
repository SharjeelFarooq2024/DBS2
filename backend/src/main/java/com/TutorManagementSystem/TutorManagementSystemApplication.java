package com.TutorManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.TutorManagementSystem.repository")
@EntityScan(basePackages = "com.TutorManagementSystem.model")
public class TutorManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TutorManagementSystemApplication.class, args);
    }
}

