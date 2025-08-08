package com.example.mostin.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "employee_md")
@IdClass(EmployeeId.class)
public class Employee {

    @Id
    private String employeeId;

    @Id
    private String employeeName;

    private String employeePwd;
    private String phoneNum;
    private String employeeType;
    private String address;
    private String workPlaceName;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
        @ToString.Exclude
    private List<Commute> commutes;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
        @ToString.Exclude
    private List<Ordering> orders;
}
