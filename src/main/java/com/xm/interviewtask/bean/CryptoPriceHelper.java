package com.xm.interviewtask.bean;

import com.xm.interviewtask.dto.SymbolData;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Helper class originally created to offload processing of cryptocurrency price records.
 * It supports getting SymbolData for all symbols, for a specific symbol or for a specific day
 */
public interface CryptoPriceHelper {
    /**
     * Get all available SymbolData for all Symbols
     * @return List of SymbolData for all available symbols
     */
    List<SymbolData> getSummarizedData();

    /**
     *
     * @param symbol - The symbol name for which to retrieve data
     * @return Optional SymbolData if found
     */
    Optional<SymbolData> getDataForSymbol(@NonNull String symbol);

    /**
     * @param requestedDate - The date for which we're requesting symbol with highest normalized range
     * @return List of SymbolData available on the requested date
     */
    List<SymbolData> getCryptoDataForDay(@NonNull LocalDate requestedDate);
}
