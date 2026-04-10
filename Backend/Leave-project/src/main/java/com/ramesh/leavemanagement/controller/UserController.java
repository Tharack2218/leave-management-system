package com.ramesh.leavemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ramesh.leavemanagement.model.User;
import com.ramesh.leavemanagement.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ✅ GET USER BY EMAIL
    @GetMapping("/by-email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}