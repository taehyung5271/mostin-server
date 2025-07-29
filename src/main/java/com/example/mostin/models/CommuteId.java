package com.example.mostin.models;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CommuteId implements Serializable {
    private LocalDate commuteDay;
    private String employeeId;
}
