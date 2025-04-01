package com.example.ks3.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GoldSmcData {
    private String type; // contract, product, equity
    private String id;
    private String symbol;
    private String name;
    private String exchange;
    private String currency;
    
    // 新增字段
    private String contractType; // fut, opt, collateral
    private String securityType;
    private String exchangeId;
    private String exchangeClearingId;
    private Integer contractSize;
    private String lastTradeDate;
    private String maturityDate;
    
    // 用于关联 product 和 contract
    private String productId;  // 当 type 为 contract 时，指向关联的 product
    // 其他相关字段
} 