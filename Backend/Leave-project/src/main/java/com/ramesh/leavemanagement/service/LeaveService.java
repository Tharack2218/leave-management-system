package com.ramesh.leavemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import com.ramesh.leavemanagement.model.LeaveRequest;
import com.ramesh.leavemanagement.repository.LeaveRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    // ✅ APPLY LEAVE (FINAL SIMPLE LOGIC)
    public LeaveRequest applyLeave(LeaveRequest leave) {

        // ✅ VALIDATIONS
        if (leave.getEmployeeName() == null || leave.getEmployeeName().isEmpty()) {
            throw new RuntimeException("Employee name is required");
        }

        if (leave.getEmployeeEmail() == null || leave.getEmployeeEmail().isEmpty()) {
            throw new RuntimeException("Employee email is required");
        }

        if (leave.getReason() == null || leave.getReason().isEmpty()) {
            throw new RuntimeException("Reason is required");
        }

        if (leave.getStartDate() == null || leave.getEndDate() == null) {
            throw new RuntimeException("Start and End dates are required");
        }

        LocalDate start = LocalDate.parse(leave.getStartDate());
        LocalDate end = LocalDate.parse(leave.getEndDate());

        if (end.isBefore(start)) {
            throw new RuntimeException("End date cannot be before start date");
        }

        // ✅ CORRECT DAY CALCULATION (RETURN DATE LOGIC)
        long newLeaveDays = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        if (newLeaveDays == 0) newLeaveDays = 1;

        long allowedDays = 3;

        // ✅ FINAL LOGIC (ONLY CURRENT REQUEST)
        leave.setCreatedAt(LocalDate.now().toString());

        if (newLeaveDays > allowedDays) {
            leave.setStatus("REJECTED");
        } else {
            leave.setStatus("PENDING");
        }

        return leaveRepository.save(leave);
    }

    // ✅ GET EMPLOYEE LEAVES
    public List<LeaveRequest> getLeavesByEmail(String email) {
        return leaveRepository.findByEmployeeEmail(email);
    }

    // ✅ GET ALL LEAVES
    public List<LeaveRequest> getAllLeaves() {
        return leaveRepository.findAll();
    }
}