package com.xm.interviewtask.dto;

import java.time.LocalDateTime;

public class SymbolRecord implements Comparable<SymbolRecord> {
    private String symbolName;
    private LocalDateTime timeStamp;
    private float price;

    public SymbolRecord(String symbolName, LocalDateTime timeStamp, float price) {
        this.symbolName = symbolName;
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public int compareTo(SymbolRecord o) {
        if (this.price < o.price) {
            return -1;
        } else if (this.price > o.price) {
            return 1;
        }
        return 0;
    }
}
