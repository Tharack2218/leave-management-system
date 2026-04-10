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

    // ✅ APPLY LEAVE (FIXED RESPONSE)
    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody LeaveRequest leave) {
        try {
            // ✅ get saved leave with status
            LeaveRequest savedLeave = leaveService.applyLeave(leave);

            // ✅ return proper message
            if ("REJECTED".equalsIgnoreCase(savedLeave.getStatus())) {
                return ResponseEntity.ok("❌ Leave request rejected (limit exceeded)");
            } else if ("PENDING".equalsIgnoreCase(savedLeave.getStatus())) {
                return ResponseEntity.ok("✅ Leave applied successfully (Pending approval)");
            } else if ("APPROVED".equalsIgnoreCase(savedLeave.getStatus())) {
                return ResponseEntity.ok("✅ Leave approved");
            }

            return ResponseEntity.ok("Leave processed");

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error applying leave");
        }
    }

    // ✅ GET EMPLOYEE LEAVES
    @GetMapping("/employee/{email}")
    public ResponseEntity<List<LeaveRequest>> getEmployeeLeaves(@PathVariable String email) {
        return ResponseEntity.ok(leaveService.getLeavesByEmail(email));
    }

    // ✅ GET ALL LEAVES (ADMIN)
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

    // ✅ OPTIONAL ALIAS
    @GetMapping("/my/{email}")
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(@PathVariable String email) {
        return ResponseEntity.ok(leaveService.getLeavesByEmail(email));
    }
}