package com.TutorManagementSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.TutorManagementSystem.dto.LoginRequest;
import com.TutorManagementSystem.dto.LoginResponse;
import com.TutorManagementSystem.dto.RegisterRequest;
import com.TutorManagementSystem.model.User;
import com.TutorManagementSystem.model.Tutor;
import com.TutorManagementSystem.model.Student;
import com.TutorManagementSystem.repository.UserRepository;
import com.TutorManagementSystem.repository.TutorRepository;
import com.TutorManagementSystem.repository.StudentRepository;
import com.TutorManagementSystem.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TutorRepository tutorRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Add detailed request logging
            System.out.println("=== USER REGISTRATION REQUEST ===");
            System.out.println("Email: " + (registerRequest.getEmail() != null ? registerRequest.getEmail() : "NULL"));
            System.out.println("Name: " + (registerRequest.getName() != null ? registerRequest.getName() : "NULL"));
            System.out.println("Role: " + (registerRequest.getRole() != null ? registerRequest.getRole() : "NULL"));
            System.out.println("Grade: " + (registerRequest.getGrade() != null ? registerRequest.getGrade() : "NULL"));
            System.out.println("Specialization: " + (registerRequest.getSpecialization() != null ? registerRequest.getSpecialization() : "NULL"));
            System.out.println("Qualifications: " + (registerRequest.getQualifications() != null ? registerRequest.getQualifications() : "NULL"));
            System.out.println("================================");

            // Check if required fields are present
            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (registerRequest.getName() == null || registerRequest.getName().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Name is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Password is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (registerRequest.getRole() == null || registerRequest.getRole().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Role is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Check if user already exists
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Email is already registered");
                return ResponseEntity.badRequest().body(response);
            }

            User savedUser = null;
            
            if ("TUTOR".equals(registerRequest.getRole())) {
                Tutor tutor = new Tutor();
                tutor.setName(registerRequest.getName());
                tutor.setEmail(registerRequest.getEmail());
                tutor.setPassword(registerRequest.getPassword()); // Consider encoding this
                tutor.setRole(registerRequest.getRole());
                // tutor.setCreatedAt(LocalDateTime.now()); // @PrePersist in User entity handles this
                tutor.setSpecialization(registerRequest.getSpecialization());
                tutor.setQualifications(registerRequest.getQualifications());
                savedUser = tutorRepository.save(tutor);
            } else if ("STUDENT".equals(registerRequest.getRole())) {
                Student student = new Student();
                student.setName(registerRequest.getName());
                student.setEmail(registerRequest.getEmail());
                student.setPassword(registerRequest.getPassword()); // Consider encoding this
                student.setRole(registerRequest.getRole());
                // student.setCreatedAt(LocalDateTime.now()); // @PrePersist in User entity handles this
                student.setGrade(registerRequest.getGrade());
                savedUser = studentRepository.save(student);
            } else {
                // Handle cases where role is neither TUTOR nor STUDENT, or throw an error
                User user = new User();
                user.setName(registerRequest.getName());
                user.setEmail(registerRequest.getEmail());
                user.setPassword(registerRequest.getPassword()); // Consider encoding this
                user.setRole(registerRequest.getRole());
                // user.setCreatedAt(LocalDateTime.now()); // @PrePersist in User entity handles this
                savedUser = userRepository.save(user);
            }

            System.out.println("=== REGISTRATION SUCCESS ===");
            System.out.println("User ID: " + savedUser.getId());
            System.out.println("Email: " + savedUser.getEmail());
            System.out.println("Role: " + savedUser.getRole());
            System.out.println("===========================");

            // Create response
            LoginResponse response = new LoginResponse();
            response.setId(savedUser.getId());
            response.setEmail(savedUser.getEmail());
            response.setName(savedUser.getName());
            response.setRole(savedUser.getRole());
            response.setMessage("Registration successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("=== REGISTRATION ERROR ===");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================");
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}