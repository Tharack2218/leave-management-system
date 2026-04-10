package com.ramesh.leavemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ramesh.leavemanagement.model.LeaveRequest;
import com.ramesh.leavemanagement.service.LeaveService;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private LeaveService leaveService;

    // ✅ APPLY LEAVE
    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody LeaveRequest leave) {
        try {
            leaveService.applyLeave(leave);
            return ResponseEntity.ok("Leave applied successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // ✅ GET EMPLOYEE LEAVES (ONLY ONE METHOD)
    @GetMapping("/employee/{email}")
    public ResponseEntity<List<LeaveRequest>> getEmployeeLeaves(@PathVariable String email) {
        return ResponseEntity.ok(leaveService.getLeavesByEmail(email));
    }

    // ✅ ADMIN - GET ALL LEAVES
    @GetMapping("/all")
    public ResponseEntity<?> getAllLeaves() {
        try {
            List<LeaveRequest> leaves = leaveService.getAllLeaves();
            return ResponseEntity.ok(leaves);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching leaves");
        }
    }

    // ✅ OPTIONAL SHORTCUT API
    @GetMapping("/my/{email}")
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(@PathVariable String email) {
        return ResponseEntity.ok(leaveService.getLeavesByEmail(email));
    }
}