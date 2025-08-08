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
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "ordering")
@IdClass(OrderingId.class)
public class Ordering {

    @Id
    private LocalDate orderingDay;

    @Id
    private String employeeId;

    @Id
    private String barcode;

    private String employeeName;
    private Integer boxNum;
    private String goodsName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "employeeId", referencedColumnName = "employeeId", insertable = false, updatable = false),
        @JoinColumn(name = "employeeName", referencedColumnName = "employeeName", insertable = false, updatable = false)
    })
        @ToString.Exclude
    private Employee employee;
}
