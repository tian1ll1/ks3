package com.example.ks3.model;

import lombok.Data;

@Data
public class BaseGoldSmcData {
    private String type; // contract, product, equity
    private String id;
    private String symbol;
    private String name;
    private String exchange;
    private String currency;
    private String securityType;
    private String exchangeId;
    private String exchangeClearingId;
} 