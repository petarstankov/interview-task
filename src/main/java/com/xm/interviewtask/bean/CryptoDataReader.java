package com.xm.interviewtask.bean;

import com.xm.interviewtask.dto.SymbolRecord;

import java.util.List;

/**
 * Interface for reading of crypto price data
 */
public interface CryptoDataReader {
    /**
     * Reads all symbol data either from in memory store or from the file system
     * @Param reload - whether to re-read the data from the disk or not
     *
     * @Returns List of SymbolRecord
     */
    List<SymbolRecord> getAllSymbolData(boolean reload);
}
