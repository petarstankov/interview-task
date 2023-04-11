package com.xm.interviewtask.response;

import com.xm.interviewtask.dto.SymbolData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Single crypto recommendation entry")
public class CryptoRecommendation {

    @ApiModelProperty(notes = "Unique identifier for the Symbol.", example = "BTC")
    private String symbol;
    @ApiModelProperty(notes = "Calculated range by the formula (max-min)/min", example = "0.649321")
    private float normalizedRange;

    @ApiModelProperty(notes = "Minimal price for the requested period.", example = "34331")
    private float minPrice;

    @ApiModelProperty(notes = "Maximum price for the requested period.", example = "45612")
    private float maxPrice;

    @ApiModelProperty(notes = "Newest price for the requested period.", example = "45712")
    private float newestPrice;

    @ApiModelProperty(notes = "Oldest price for the selected period.", example = "42821")
    private float oldestPrice;

    public CryptoRecommendation(SymbolData symbolData) {
        this.symbol = symbolData.getSymbolName();
        this.normalizedRange = symbolData.getNormalizedRange();
        this.minPrice = symbolData.getMinPrice();
        this.maxPrice = symbolData.getMaxPrice();
        this.newestPrice = symbolData.getNewestPrice();
        this.oldestPrice = symbolData.getOldestPrice();
    }

    public String getSymbol() {
        return symbol;
    }

    public float getNormalizedRange() {
        return normalizedRange;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public float getNewestPrice() {
        return newestPrice;
    }

    public float getOldestPrice() {
        return oldestPrice;
    }
}
