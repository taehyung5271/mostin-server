package com.example.mostin.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class DebugTest {

    @Test
    void debugToString() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setWorkPlaceName("본사");
        workPlace.setLatitude(37.5665);
        workPlace.setLongitude(126.9780);
        System.out.println("WorkPlace toString: " + workPlace.toString());
        
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setEmployeeName("김철수");
        employee.setEmployeePwd("password123");
        System.out.println("Employee toString: " + employee.toString());
        
        EmployeeId employeeId = new EmployeeId();
        employeeId.setEmployeeId("EMP001");
        employeeId.setEmployeeName("김철수");
        System.out.println("EmployeeId toString: " + employeeId.toString());
        
        // Test equals behavior
        Employee emp1 = new Employee();
        emp1.setEmployeeId("EMP001");
        emp1.setEmployeeName("김철수");
        emp1.setEmployeePwd("password123");
        
        Employee emp2 = new Employee();
        emp2.setEmployeeId("EMP001");
        emp2.setEmployeeName("김철수");
        emp2.setEmployeePwd("different");
        
        System.out.println("Employee equals (different pwd): " + emp1.equals(emp2));
        System.out.println("Employee hashCode 1: " + emp1.hashCode());
        System.out.println("Employee hashCode 2: " + emp2.hashCode());
        
        // Test WorkPlace equals
        WorkPlace wp1 = new WorkPlace();
        wp1.setWorkPlaceName("본사");
        wp1.setLatitude(37.5665);
        wp1.setLongitude(126.9780);
        
        WorkPlace wp2 = new WorkPlace();
        wp2.setWorkPlaceName("본사");
        wp2.setLatitude(35.1796);
        wp2.setLongitude(129.0756);
        
        System.out.println("WorkPlace equals (same name, different coords): " + wp1.equals(wp2));
        System.out.println("WorkPlace hashCode 1: " + wp1.hashCode());
        System.out.println("WorkPlace hashCode 2: " + wp2.hashCode());
    }
}