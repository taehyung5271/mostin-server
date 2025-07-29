package com.example.mostin.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.FetchType;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "commute")
@IdClass(CommuteId.class)
public class Commute {

    @Id
    private LocalDate commuteDay;

    @Id
    private String employeeId;

    private String employeeName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String workPlaceName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "employeeId", referencedColumnName = "employeeId", insertable = false, updatable = false),
        @JoinColumn(name = "employeeName", referencedColumnName = "employeeName", insertable = false, updatable = false)
    })
    private Employee employee;
}
