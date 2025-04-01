package com.example.ks3.model;

import lombok.Data;

@Data
public class GoldSmcMessage {
    private String type;  // product, contract, equity
    private Object data;  // The actual message data

    public boolean isProduct() {
        return "product".equals(type) && data instanceof Product;
    }

    public boolean isContract() {
        return "contract".equals(type) && data instanceof Contract;
    }

    public boolean isEquity() {
        return "equity".equals(type) && data instanceof BaseGoldSmcData;
    }

    public Product asProduct() {
        return isProduct() ? (Product) data : null;
    }

    public Contract asContract() {
        return isContract() ? (Contract) data : null;
    }

    public BaseGoldSmcData asEquity() {
        return isEquity() ? (BaseGoldSmcData) data : null;
    }

    public boolean isValid() {
        return type != null && data != null && 
               (isProduct() || isContract() || isEquity());
    }
} 