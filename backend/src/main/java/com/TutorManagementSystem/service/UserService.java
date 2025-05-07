package com.TutorManagementSystem.service;

import com.TutorManagementSystem.dto.LoginResponse;
import com.TutorManagementSystem.dto.RegisterRequest;
import com.TutorManagementSystem.model.User;
import com.TutorManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public LoginResponse login(String email, String password) {
        try {
            // Add debugging for finding the user
            System.out.println("Attempting to find user with email: " + email);
            
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (!userOpt.isPresent()) {
                System.out.println("User not found with email: " + email);
                throw new RuntimeException("User not found with email: " + email);
            }
            
            User user = userOpt.get();
            
            // Debug password check
            System.out.println("Found user: " + user.getName() + " with role: " + user.getRole());
            System.out.println("Password check: Input=" + password + ", Stored=" + user.getPassword());
            
            if (!password.equals(user.getPassword())) {
                System.out.println("Password mismatch for user: " + email);
                throw new RuntimeException("Invalid password");
            }

            // Success - create response
            LoginResponse response = new LoginResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());
            
            System.out.println("Login successful for: " + email);
            return response;
        } catch (Exception e) {
            // Log the error for server-side debugging
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User register(RegisterRequest registerRequest) {
        // Create a new User
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());
        user.setCreatedAt(java.time.LocalDateTime.now());
        
        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
