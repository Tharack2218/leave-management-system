package com.ramesh.leavemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ramesh.leavemanagement.model.LeaveRequest;
import com.ramesh.leavemanagement.model.User;
import com.ramesh.leavemanagement.repository.LeaveRepository;
import com.ramesh.leavemanagement.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    // ✅ GET ALL EMPLOYEES
    @GetMapping("/employees")
    public ResponseEntity<?> getAllEmployees() {
        try {
            List<User> users = userRepository.findByRole("EMPLOYEE");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(List.of()); // safe fallback
        }
    }

    // ✅ GET EMPLOYEE COUNT
    @GetMapping("/employee-count")
    public ResponseEntity<?> getEmployeeCount() {
        try {
            long count = userRepository.countByRole("EMPLOYEE");
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(0);
        }
    }

    // ✅ GET PENDING USERS
    @GetMapping("/pending-users")
    public ResponseEntity<?> getPendingUsers() {
        try {
            List<User> users = userRepository.findByActiveFalse();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(List.of());
        }
    }

    // ✅ APPROVE USER
    @PutMapping("/approve-user/{id}")
    public ResponseEntity<?> approveUser(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setActive(true);
            userRepository.save(user);

            return ResponseEntity.ok("User approved");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving user");
        }
    }

    // ✅ GET ALL LEAVES
    @GetMapping("/leaves")
    public ResponseEntity<?> getAllLeaves() {
        try {
            List<LeaveRequest> leaves = leaveRepository.findAll();
            return ResponseEntity.ok(leaves);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(List.of());
        }
    }

    // ✅ APPROVE LEAVE
    @PutMapping("/approve-leave/{id}")
    public ResponseEntity<?> approveLeave(@PathVariable Long id) {
        try {
            LeaveRequest leave = leaveRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Leave not found"));

            leave.setStatus("APPROVED");
            leaveRepository.save(leave);

            return ResponseEntity.ok("Leave approved");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving leave");
        }
    }

    // ✅ REJECT LEAVE
    @PutMapping("/reject-leave/{id}")
    public ResponseEntity<?> rejectLeave(@PathVariable Long id) {
        try {
            LeaveRequest leave = leaveRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Leave not found"));

            leave.setStatus("REJECTED");
            leaveRepository.save(leave);

            return ResponseEntity.ok("Leave rejected");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rejecting leave");
        }
    }
}