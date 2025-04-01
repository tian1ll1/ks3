package com.example.ks3.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Product extends BaseGoldSmcData {
    private String productType;
    private String underlyingSymbol;
    private String optionType; // call, put
    private Double strikePrice;
    private String currency;
} 