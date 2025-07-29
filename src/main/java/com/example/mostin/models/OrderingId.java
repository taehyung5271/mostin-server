package com.example.mostin.models;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class OrderingId implements Serializable {
    private LocalDate orderingDay;
    private String employeeId;
    private String barcode;
}
