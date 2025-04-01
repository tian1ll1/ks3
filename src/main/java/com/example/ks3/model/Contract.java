package com.example.ks3.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Contract extends BaseGoldSmcData {
    private String contractType; // fut, opt, collateral
    private Integer contractSize;
    private String lastTradeDate;
    private String maturityDate;
    private String productId;  // 指向关联的 product
} 