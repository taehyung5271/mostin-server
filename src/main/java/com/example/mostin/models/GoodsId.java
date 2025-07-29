package com.example.mostin.models;

import java.io.Serializable;
import lombok.Data;

@Data
public class GoodsId implements Serializable {
    private String barcode;
    private String goodsName;
}
