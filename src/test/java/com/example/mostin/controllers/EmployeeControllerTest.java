package com.example.mostin.controllers;

import com.example.mostin.models.Employee;
import com.example.mostin.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee testEmployee;
    private Map<String, String> loginCredentials;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setEmployeeId("EMP001");
        testEmployee.setEmployeeName("John Doe");
        testEmployee.setEmployeePwd("$2a$10$encodedPassword");
        testEmployee.setPhoneNum("010-1234-5678");
        testEmployee.setEmployeeType("FULL_TIME");
        testEmployee.setAddress("Seoul, Korea");
        testEmployee.setWorkPlaceName("Main Office");

        loginCredentials = new HashMap<>();
        loginCredentials.put("employeeId", "EMP001");
        loginCredentials.put("password", "plainPassword");
    }

    @Test
    void should_returnEmployee_when_loginWithValidCredentials() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testEmployee));
        when(passwordEncoder.matches("plainPassword", "$2a$10$encodedPassword")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginCredentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.employeeName").value("John Doe"))
                .andExpect(jsonPath("$.workPlaceName").value("Main Office"));

        verify(employeeRepository).findByEmployeeId("EMP001");
        verify(passwordEncoder).matches("plainPassword", "$2a$10$encodedPassword");
    }

    @Test
    void should_return401_when_loginWithInvalidPassword() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testEmployee));
        when(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword")).thenReturn(false);

        loginCredentials.put("password", "wrongPassword");

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginCredentials)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Password mismatch"));

        verify(employeeRepository).findByEmployeeId("EMP001");
        verify(passwordEncoder).matches("wrongPassword", "$2a$10$encodedPassword");
    }

    @Test
    void should_return404_when_loginWithNonExistentEmployeeId() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("NONEXISTENT")).thenReturn(Optional.empty());

        loginCredentials.put("employeeId", "NONEXISTENT");

        // When & Then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginCredentials)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(employeeRepository).findByEmployeeId("NONEXISTENT");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void should_createEmployeeWithEncodedPassword_when_validEmployeeProvided() throws Exception {
        // Given
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId("EMP002");
        newEmployee.setEmployeeName("Jane Smith");
        newEmployee.setEmployeePwd("plainPassword");
        newEmployee.setPhoneNum("010-9876-5432");

        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId("EMP002");
        savedEmployee.setEmployeeName("Jane Smith");
        savedEmployee.setEmployeePwd("$2a$10$encodedPassword");
        savedEmployee.setPhoneNum("010-9876-5432");

        when(passwordEncoder.encode("plainPassword")).thenReturn("$2a$10$encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP002"))
                .andExpect(jsonPath("$.employeeName").value("Jane Smith"))
                .andExpect(jsonPath("$.employeePwd").value("$2a$10$encodedPassword"));

        verify(passwordEncoder).encode("plainPassword");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void should_returnAllEmployees_when_getAllEmployeesRequested() throws Exception {
        // Given
        Employee employee2 = new Employee();
        employee2.setEmployeeId("EMP002");
        employee2.setEmployeeName("Jane Smith");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(testEmployee, employee2));

        // When & Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].employeeId").value("EMP001"))
                .andExpect(jsonPath("$[1].employeeId").value("EMP002"));

        verify(employeeRepository).findAll();
    }

    @Test
    void should_returnEmployee_when_getEmployeeByValidId() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testEmployee));

        // When & Then
        mockMvc.perform(get("/api/employees/EMP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.employeeName").value("John Doe"));

        verify(employeeRepository).findByEmployeeId("EMP001");
    }

    @Test
    void should_return404_when_getEmployeeByInvalidId() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("INVALID")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/employees/INVALID"))
                .andExpect(status().isNotFound());

        verify(employeeRepository).findByEmployeeId("INVALID");
    }

    @Test
    void should_updateEmployeeWithEncodedPassword_when_validUpdateData() throws Exception {
        // Given
        Employee updateData = new Employee();
        updateData.setEmployeeName("John Updated");
        updateData.setPhoneNum("010-1111-2222");
        updateData.setWorkPlaceName("Updated Office");
        updateData.setEmployeePwd("newPassword");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeId("EMP001");
        updatedEmployee.setEmployeeName("John Updated");
        updatedEmployee.setPhoneNum("010-1111-2222");
        updatedEmployee.setWorkPlaceName("Updated Office");
        updatedEmployee.setEmployeePwd("$2a$10$newEncodedPassword");

        when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testEmployee));
        when(passwordEncoder.encode("newPassword")).thenReturn("$2a$10$newEncodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // When & Then
        mockMvc.perform(put("/api/employees/EMP001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("John Updated"))
                .andExpect(jsonPath("$.phoneNum").value("010-1111-2222"))
                .andExpect(jsonPath("$.workPlaceName").value("Updated Office"));

        verify(employeeRepository).findByEmployeeId("EMP001");
        verify(passwordEncoder).encode("newPassword");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void should_updateEmployeeWithoutPasswordChange_when_passwordNotProvided() throws Exception {
        // Given
        Employee updateData = new Employee();
        updateData.setEmployeeName("John Updated");
        updateData.setPhoneNum("010-1111-2222");

        when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // When & Then
        mockMvc.perform(put("/api/employees/EMP001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        verify(employeeRepository).findByEmployeeId("EMP001");
        verify(passwordEncoder, never()).encode(anyString());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void should_return404_when_updateNonExistentEmployee() throws Exception {
        // Given
        Employee updateData = new Employee();
        updateData.setEmployeeName("Updated Name");

        when(employeeRepository.findByEmployeeId("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/employees/NONEXISTENT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());

        verify(employeeRepository).findByEmployeeId("NONEXISTENT");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void should_deleteEmployee_when_validEmployeeIdProvided() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testEmployee));

        // When & Then
        mockMvc.perform(delete("/api/employees/EMP001"))
                .andExpect(status().isOk());

        verify(employeeRepository).findByEmployeeId("EMP001");
        verify(employeeRepository).delete(testEmployee);
    }

    @Test
    void should_return404_when_deleteNonExistentEmployee() throws Exception {
        // Given
        when(employeeRepository.findByEmployeeId("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/employees/NONEXISTENT"))
                .andExpect(status().isNotFound());

        verify(employeeRepository).findByEmployeeId("NONEXISTENT");
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}