package com.example.mostin.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "goods")
@IdClass(GoodsId.class)
public class Goods {

    @Id
    private String barcode;

    @Id
    private String goodsName;
}
