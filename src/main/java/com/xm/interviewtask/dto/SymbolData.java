package com.xm.interviewtask.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SymbolData {
    private String symbolName;
    private float oldestPrice;
    private float newestPrice;
    private float minPrice = Float.MAX_VALUE;
    private float maxPrice = Float.MIN_VALUE;

    private float normalizedRange;

    public SymbolData(List<SymbolRecord> symbolRecords) {
        LocalDateTime oldest = LocalDateTime.MAX;
        LocalDateTime newest = LocalDateTime.MIN;
        for (SymbolRecord symbolRecord : symbolRecords) {
            this.symbolName = symbolRecord.getSymbolName();

            if (symbolRecord.getTimeStamp().isBefore(oldest)) {
                this.oldestPrice = symbolRecord.getPrice();
                oldest = symbolRecord.getTimeStamp();
            }

            if (symbolRecord.getTimeStamp().isAfter(newest)) {
                this.newestPrice = symbolRecord.getPrice();
                newest = symbolRecord.getTimeStamp();
            }

            if (symbolRecord.getPrice() > this.maxPrice) {
                this.maxPrice = symbolRecord.getPrice();
            }

            if (symbolRecord.getPrice() < this.minPrice) {
                this.minPrice = symbolRecord.getPrice();
            }

            this.normalizedRange = (this.maxPrice - this.minPrice)/this.minPrice;
        }
    }

    public float getOldestPrice() {
        return oldestPrice;
    }

    public float getNewestPrice() {
        return newestPrice;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public float getNormalizedRange() {
        return normalizedRange;
    }

    public String getSymbolName() { return symbolName; }
}
