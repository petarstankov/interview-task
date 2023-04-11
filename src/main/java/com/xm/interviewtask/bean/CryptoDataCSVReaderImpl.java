package com.xm.interviewtask.bean;

import com.xm.interviewtask.dto.SymbolRecord;
import com.xm.interviewtask.exception.DataInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Component
/*
  CryptoDataCSVReaderImpl is an implementation of the CryptoDataReader
  It's sole responsibility is to read all the csv files present under location
  defined in the "prices.filepath" parameter (default = classpath:prices/*_values.csv)

  In case of any error while reading the files, the application will not start.
 */
public class CryptoDataCSVReaderImpl implements CryptoDataReader {
    @Value("${prices.filepath:classpath:prices/*_values.csv}")
    private String csvFolder;

    private final List<SymbolRecord> symbolData = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(CryptoDataCSVReaderImpl.class);

    @Override
     public List<SymbolRecord> getAllSymbolData(boolean reload) {
        if (reload) {
            reloadData();
        }

        return this.symbolData;
    }

    @PostConstruct
    private void init() {
        readCSVs();
    }

    private synchronized void reloadData() {
        symbolData.clear();
        readCSVs();
    }

    private synchronized void readCSVs() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            System.out.println("CSV Folder is: " + csvFolder);
            Resource[] resources = resolver.getResources(csvFolder);

            for (Resource res : resources) {
                processFile(res.getFile());
            }
        } catch (IOException e) {
            logger.error("Error initializing application: ", e);
            throw new DataInitializationException();
        }
    }

    private void processFile(File csvFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            Stream<String> lines = reader.lines().skip(1);
            lines.forEach(this::processLine);
        } catch (IOException e) {
            logger.error("Error reading symbol data from file: {}", csvFile.getName(), e);
            throw new DataInitializationException();
        }
    }

    private void processLine(String line) {
        try {
            String[] parts = line.split(",");

            LocalDateTime timestamp =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(parts[0].trim())),
                            TimeZone.getDefault().toZoneId());
            String symbol = parts[1].trim().toUpperCase();
            float price = Float.parseFloat(parts[2].trim());
            SymbolRecord symbolRecord = new SymbolRecord(symbol, timestamp, price);
            symbolData.add(symbolRecord);
        } catch (Exception e) {
            logger.error("Parsing error for line data: {}", line, e);
            throw new DataInitializationException();
        }
    }
}
