package com.ramesh.leavemanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramesh.leavemanagement.model.LeaveRequest;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    // ✅ Fetch by employee email (MAIN FIX)
    List<LeaveRequest> findByEmployeeEmail(String employeeEmail);

    // ✅ Optional: fetch by name (keep only if needed)
    List<LeaveRequest> findByEmployeeName(String employeeName);
    
    
}

