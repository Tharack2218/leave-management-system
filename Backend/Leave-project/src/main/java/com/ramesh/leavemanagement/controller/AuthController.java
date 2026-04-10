package com.ramesh.leavemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

import com.ramesh.leavemanagement.model.User;
import com.ramesh.leavemanagement.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Email is required"));
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Password is required"));
            }

            Optional<User> existing = userRepository.findByEmailIgnoreCase(user.getEmail().trim());

            if (existing.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Email already registered"));
            }

            // ✅ DEFAULT VALUES
            user.setEmail(user.getEmail().trim());
            user.setRole("EMPLOYEE");

            // 🔥 VERY IMPORTANT CHANGE
            user.setActive(false);  // ⛔ wait for admin approval

            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "Registered. Wait for admin approval"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Registration failed"));
        }
    }
    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // ✅ null checks
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Email is required"));
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Password is required"));
            }

            String email = user.getEmail().trim();
            String password = user.getPassword().trim();

            System.out.println("Login attempt: " + email);

            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

            if (optionalUser.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found"));
            }

            User dbUser = optionalUser.get();

            // ✅ password check
            if (dbUser.getPassword() == null || !dbUser.getPassword().equals(password)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid password"));
            }

            // ✅ active check
            if (!dbUser.isActive()) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Account not approved"));
            }

            // ✅ FINAL RESPONSE (frontend-friendly)
            return ResponseEntity.ok(
                    Map.of(
                            "email", dbUser.getEmail(),
                            "role", dbUser.getRole()
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Login failed"));
        }
    }
}