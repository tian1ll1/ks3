package com.example.ks3.util;

import com.example.ks3.model.PositionInstrument;
import com.example.ks3.model.Product;
import com.example.ks3.model.Contract;
import com.example.ks3.model.BaseGoldSmcData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {
    
    public static List<PositionInstrument> generatePositionInstruments() {
        List<PositionInstrument> positions = new ArrayList<>();
        
        // 期货合约
        PositionInstrument fut1 = new PositionInstrument();
        fut1.setId("FUT001");
        fut1.setSymbol("GC");
        fut1.setType("FUTURE");
        fut1.setExchange("COMEX");
        fut1.setCurrency("USD");
        fut1.setContractType("fut");
        fut1.setSecurityType("FUTURE");
        fut1.setExchangeId("CMX");
        fut1.setExchangeClearingId("GC");
        fut1.setContractSize(100);
        fut1.setLastTradeDate(LocalDate.of(2024, 12, 31));
        positions.add(fut1);

        // 期权合约
        PositionInstrument opt1 = new PositionInstrument();
        opt1.setId("OPT001");
        opt1.setSymbol("GC");
        opt1.setType("OPTION");
        opt1.setExchange("COMEX");
        opt1.setCurrency("USD");
        opt1.setContractType("opt");
        opt1.setSecurityType("OPTION");
        opt1.setExchangeId("CMX");
        opt1.setExchangeClearingId("GC");
        opt1.setContractSize(100);
        opt1.setLastTradeDate(LocalDate.of(2024, 12, 31));
        positions.add(opt1);

        return positions;
    }

    public static List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        
        Product gold = new Product();
        gold.setId("GOLD");
        gold.setType("product");
        gold.setName("Gold");
        gold.setSecurityType("OPTION");
        gold.setExchangeId("CMX");
        gold.setExchangeClearingId("GC");
        products.add(gold);

        return products;
    }

    public static List<Contract> generateContracts() {
        List<Contract> contracts = new ArrayList<>();
        
        // 期货合约
        Contract fut1 = new Contract();
        fut1.setId("FUT001");
        fut1.setType("contract");
        fut1.setName("Gold Futures");
        fut1.setContractType("fut");
        fut1.setSecurityType("FUTURE");
        fut1.setExchangeId("CMX");
        fut1.setExchangeClearingId("GC");
        fut1.setContractSize(100);
        fut1.setLastTradeDate(LocalDate.of(2024, 12, 31));
        contracts.add(fut1);

        // 期权合约
        Contract opt1 = new Contract();
        opt1.setId("OPT001");
        opt1.setType("contract");
        opt1.setName("Gold Options");
        opt1.setContractType("opt");
        opt1.setSecurityType("OPTION");
        opt1.setExchangeId("CMX");
        opt1.setExchangeClearingId("GC");
        opt1.setContractSize(100);
        opt1.setLastTradeDate(LocalDate.of(2024, 12, 31));
        opt1.setProductId("GOLD");  // 关联到产品
        contracts.add(opt1);

        return contracts;
    }

    public static List<BaseGoldSmcData> generateGoldSmcData() {
        List<BaseGoldSmcData> data = new ArrayList<>();
        data.addAll(generateProducts());
        data.addAll(generateContracts());
        return data;
    }
} 