package com.ramesh.leavemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.ramesh.leavemanagement.model.LeaveRequest;
import com.ramesh.leavemanagement.repository.LeaveRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    // ✅ APPLY LEAVE WITH AUTO REJECT LOGIC
    public void applyLeave(LeaveRequest leave) {

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

        int currentMonth = start.getMonthValue();
        int currentYear = start.getYear();

        // ✅ GET USER LEAVES
        List<LeaveRequest> userLeaves =
                leaveRepository.findByEmployeeEmail(leave.getEmployeeEmail());

        // 🔥 IMPORTANT: SORT BY DATE (VERY IMPORTANT FIX)
        userLeaves.sort(Comparator.comparing(l -> LocalDate.parse(l.getStartDate())));

        // ✅ CALCULATE EXISTING DAYS
        long totalDays = userLeaves.stream()
                .filter(l -> {
                    LocalDate d = LocalDate.parse(l.getStartDate());
                    return d.getMonthValue() == currentMonth &&
                           d.getYear() == currentYear;
                })
                .mapToLong(l -> {
                    LocalDate s = LocalDate.parse(l.getStartDate());
                    LocalDate e = LocalDate.parse(l.getEndDate());
                    return java.time.temporal.ChronoUnit.DAYS.between(s, e) + 1;
                })
                .sum();

        long newLeaveDays =
                java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

        long allowedDays = 3;

        // 🔥 STEP 1: AUTO REJECT OLD EXTRA PENDING LEAVES
        long runningDays = 0;

        for (LeaveRequest l : userLeaves) {

            LocalDate s = LocalDate.parse(l.getStartDate());
            LocalDate e = LocalDate.parse(l.getEndDate());

            if (s.getMonthValue() == currentMonth && s.getYear() == currentYear) {

                long days = java.time.temporal.ChronoUnit.DAYS.between(s, e) + 1;

                runningDays += days;

                if (runningDays > allowedDays && "PENDING".equals(l.getStatus())) {
                    l.setStatus("REJECTED");
                    leaveRepository.save(l);
                }
            }
        }

        // 🔥 STEP 2: HANDLE NEW LEAVE
        leave.setCreatedAt(LocalDate.now().toString());

        if (totalDays + newLeaveDays > allowedDays) {
            leave.setStatus("REJECTED");
        } else {
            leave.setStatus("PENDING");
        }

        leaveRepository.save(leave);
    }

    // ✅ GET EMPLOYEE LEAVES
    public List<LeaveRequest> getLeavesByEmail(String email) {
        return leaveRepository.findByEmployeeEmail(email);
    }

    // ✅ GET ALL LEAVES (FOR ADMIN)
    public List<LeaveRequest> getAllLeaves() {
        return leaveRepository.findAll();
    }
}