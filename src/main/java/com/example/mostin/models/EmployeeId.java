package com.example.mostin.models;

import java.io.Serializable;
import lombok.Data;

@Data
public class EmployeeId implements Serializable {
    private String employeeId;
    private String employeeName;
}
