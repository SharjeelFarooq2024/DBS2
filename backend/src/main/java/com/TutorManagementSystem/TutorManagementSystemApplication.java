package com.TutorManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.TutorManagementSystem")
public class TutorManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorManagementSystemApplication.class, args);
    }
}

