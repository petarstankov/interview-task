package com.xm.interviewtask.bean;

import com.xm.interviewtask.dto.SymbolData;
import com.xm.interviewtask.dto.SymbolRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CryptoPriceHelperImpl implements CryptoPriceHelper {
    @Autowired
    private CryptoDataReader reader;

    @Value("${prices.refresh.interval:300}")
    private Long refreshInterval;

    private Long lastReloadTimestamp = System.currentTimeMillis();

    private static final Object syncObject = new Object();

    private final List<SymbolData> symbolDataList = new ArrayList<>();

    private final Map<LocalDate,List<SymbolData>> symbolDataByDateMap = new HashMap<>();

    @Override
    public List<SymbolData> getSummarizedData() {
        initData();
        return symbolDataList;
    }

    @Override
    public Optional<SymbolData> getDataForSymbol(@NonNull String symbol) {
        initData();

        return symbolDataList.stream()
                .filter(symbolData -> symbolData.getSymbolName().equalsIgnoreCase(symbol))
                .findFirst();
    }

    @Override
    public List<SymbolData> getCryptoDataForDay(@NonNull LocalDate requestedDate) {
        initData();
        List<SymbolData> result = new ArrayList<>();

        if (symbolDataByDateMap.containsKey(requestedDate)) {
            result = symbolDataByDateMap.get(requestedDate);
        }

        return result;
    }

    private void initData() {
        boolean reloadData = System.currentTimeMillis() - lastReloadTimestamp > refreshInterval*1000;
        if  (reloadData) {
            synchronized (syncObject) {
                symbolDataList.clear();
                symbolDataByDateMap.clear();
            }
        }

        if (symbolDataList.isEmpty()) {
            synchronized (syncObject) {
                List<SymbolRecord> allSymbolRecords = reader.getAllSymbolData(reloadData);
                processSymbolRecords(allSymbolRecords);
                processSymbolRecordsByDate(allSymbolRecords);
                lastReloadTimestamp = System.currentTimeMillis();
            }
        }
    }

    private void processSymbolRecords(List<SymbolRecord> allSymbolRecords) {
        Map<String, List<SymbolRecord>> symbolRecordMap = allSymbolRecords.stream().collect(Collectors.groupingBy(SymbolRecord::getSymbolName));
        for (Map.Entry<String, List<SymbolRecord>> ent : symbolRecordMap.entrySet()) {
            SymbolData symbolData = new SymbolData(ent.getValue());
            symbolDataList.add(symbolData);
        }
    }

    private void processSymbolRecordsByDate(List<SymbolRecord> allSymbolRecords) {
        Map<LocalDate,List<SymbolRecord>> symbolRecordByDateMap = allSymbolRecords.stream()
                .collect(Collectors.groupingBy(symRecord -> symRecord.getTimeStamp().toLocalDate()));
        for (Map.Entry<LocalDate, List<SymbolRecord>> ent : symbolRecordByDateMap.entrySet()) {
            Map<String, List<SymbolRecord>> symbolRecordMapForDate = ent.getValue().stream().collect(Collectors.groupingBy(SymbolRecord::getSymbolName));
            //I don't like how this one ended up... might refactor if time permits and I don't forget
            for (Map.Entry<String,List<SymbolRecord>> innerEntry : symbolRecordMapForDate.entrySet()) {
                SymbolData symbolData = new SymbolData(innerEntry.getValue());
                List<SymbolData> refinedSymbolDataList = symbolDataByDateMap.getOrDefault(ent.getKey(),new ArrayList<>());
                refinedSymbolDataList.add(symbolData);
                symbolDataByDateMap.put(ent.getKey(), refinedSymbolDataList);

            }
        }
    }
}
