package com.example.mostin.repositories;

import com.example.mostin.models.Employee;
import com.example.mostin.models.EmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, EmployeeId> {
    Optional<Employee> findByEmployeeId(String employeeId);
}
