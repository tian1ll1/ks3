package com.example.ks3.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PositionInstrument {
    private String id;
    private String symbol;
    private String type;
    private String exchange;
    private String currency;
    private String name;
    // 其他需要被富化的字段
    
    // 新增字段
    private String contractType; // fut, opt, collateral
    private String securityType;
    private String exchangeId;
    private String exchangeClearingId;
    private Integer contractSize;
    private String lastTradeDate;
    private String maturityDate;
    private String productId;  // 用于期权合约的join
} 