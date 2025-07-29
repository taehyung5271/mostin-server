package com.example.mostin.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "work_place")
public class WorkPlace {

    @Id
    private String workPlaceName;

    private double latitude;
    private double longitude;
}
