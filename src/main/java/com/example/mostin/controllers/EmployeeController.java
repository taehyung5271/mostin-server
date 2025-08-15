package com.example.mostin.controllers;

import com.example.mostin.models.Employee;
import com.example.mostin.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String employeeId = credentials.get("employeeId");
        String rawPassword = credentials.get("password");

        return employeeRepository.findByEmployeeId(employeeId)
                .map(employee -> {
                    if (passwordEncoder.matches(rawPassword, employee.getEmployeePwd())) {
                        return ResponseEntity.ok(employee);
                    }
                    return ResponseEntity.status(401).body("Password mismatch");
                })
                .orElse(ResponseEntity.status(404).body("User not found"));
    }

    // Employee CRUD
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        employee.setEmployeePwd(passwordEncoder.encode(employee.getEmployeePwd())); // Hash the password
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        return employeeRepository.findByEmployeeId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee employeeDetails) {
        return employeeRepository.findByEmployeeId(id)
                .map(employee -> {
                    employee.setEmployeeName(employeeDetails.getEmployeeName());
                    employee.setPhoneNum(employeeDetails.getPhoneNum());
                    employee.setWorkPlaceName(employeeDetails.getWorkPlaceName());
                    // Only update password if provided and different
                    if (employeeDetails.getEmployeePwd() != null && !employeeDetails.getEmployeePwd().isEmpty()) {
                        employee.setEmployeePwd(passwordEncoder.encode(employeeDetails.getEmployeePwd()));
                    }
                    // Add other fields to update as needed
                    Employee updatedEmployee = employeeRepository.save(employee);
                    return ResponseEntity.ok(updatedEmployee);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        return employeeRepository.findByEmployeeId(id)
                .map(employee -> {
                    employeeRepository.delete(employee);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employees/count")
    public ResponseEntity<Long> getEmployeeCount() {
        long count = employeeRepository.count();
        return ResponseEntity.ok(count);
    }
}
