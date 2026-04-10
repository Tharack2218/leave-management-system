package com.ramesh.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import com.ramesh.leavemanagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findByActiveFalse();
    long countByRole(String role);
    List<User> findByRole(String role);
    
    
}