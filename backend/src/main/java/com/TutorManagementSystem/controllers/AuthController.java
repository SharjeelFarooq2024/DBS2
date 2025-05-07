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
            // Check if user already exists
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Email is already registered");
                return ResponseEntity.badRequest().body(response);
            }

            // Create user
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword());
            user.setRole(registerRequest.getRole());
            user.setCreatedAt(LocalDateTime.now());
            
            // Save the user first to get the ID
            User savedUser = userRepository.save(user);
            
            // If the role is TUTOR, create a tutor record
            if ("TUTOR".equals(registerRequest.getRole())) {
                // Create tutor with same ID
                Tutor tutor = new Tutor();
                tutor.setId(savedUser.getId());
                // No need to set user field since we're using inheritance
                tutorRepository.save(tutor);
            }
            
            // If the role is STUDENT, create a student record
            if ("STUDENT".equals(registerRequest.getRole())) {
                Student student = new Student();
                student.setId(savedUser.getId());
                studentRepository.save(student);
            }

            // Create response
            LoginResponse response = new LoginResponse();
            response.setId(savedUser.getId());
            response.setEmail(savedUser.getEmail());
            response.setName(savedUser.getName());
            response.setRole(savedUser.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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