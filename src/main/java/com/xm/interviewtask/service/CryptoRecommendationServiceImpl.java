package com.xm.interviewtask.service;

import com.xm.interviewtask.response.CryptoRecommendation;
import com.xm.interviewtask.dto.SymbolData;
import com.xm.interviewtask.exception.SymbolDataNotFoundException;
import com.xm.interviewtask.bean.CryptoPriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CryptoRecommendationServiceImpl implements CryptoRecommendationService {
    @Autowired
    private CryptoPriceHelper priceHelper;

    @Override
    public List<CryptoRecommendation> getAllRecommendations() {
        List<SymbolData> symbolDataList = priceHelper.getSummarizedData();

        List<CryptoRecommendation> recommendations = new ArrayList<>();
        for (SymbolData symbolData : symbolDataList) {
            CryptoRecommendation recommendation = new CryptoRecommendation(symbolData);
            recommendations.add(recommendation);
        }

        sortRecommendations(recommendations);

        return recommendations;
    }

    @Override
    public CryptoRecommendation getRecommendationBySymbol(@NonNull String symbol) {
        Optional<SymbolData> symbolData = priceHelper.getDataForSymbol(symbol);
        if (symbolData.isEmpty()) {
            throw new SymbolDataNotFoundException();
        }
        return new CryptoRecommendation(symbolData.get());
    }

    @Override
    public CryptoRecommendation getHighestRecommendationByDay(@NonNull LocalDate date) {
        List<SymbolData> symbolDataList = priceHelper.getCryptoDataForDay(date);

        List<CryptoRecommendation> result = new ArrayList<>();

        for (SymbolData symbolData : symbolDataList) {
            CryptoRecommendation recommendation = new CryptoRecommendation(symbolData);
            result.add(recommendation);
        }

        if (result.isEmpty()) {
            throw new SymbolDataNotFoundException();
        }

        sortRecommendations(result);
        return result.get(0);
    }

    private void sortRecommendations(List<CryptoRecommendation> recommendations) {
        Comparator<CryptoRecommendation> comparator = Comparator.comparing(CryptoRecommendation::getNormalizedRange).reversed();

        recommendations.sort(comparator);
    }
}
