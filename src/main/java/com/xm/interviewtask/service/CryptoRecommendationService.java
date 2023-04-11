package com.xm.interviewtask.service;

import com.xm.interviewtask.response.CryptoRecommendation;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for retrieving CryptoRecommendations
 */
public interface CryptoRecommendationService {
    /**
     * @return List of CryptoRecommendation based on all available data, sorted in a descending order by normalizedRange
     */
    List<CryptoRecommendation> getAllRecommendations();

    /**
     * @return A single CryptoRecommendation matching the requested symbol,  based on all available data for it
     */
    CryptoRecommendation getRecommendationBySymbol(@NonNull String symbol);

    /**
     * @return A single CryptoRecommendation, based on data available for the requested date, with the highest normalizedRange
     */
    CryptoRecommendation getHighestRecommendationByDay(@NonNull LocalDate date);
}
