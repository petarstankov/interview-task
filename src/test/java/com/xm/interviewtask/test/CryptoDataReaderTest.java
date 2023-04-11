package com.xm.interviewtask.test;

import com.xm.interviewtask.dto.SymbolRecord;
import com.xm.interviewtask.bean.CryptoDataReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CryptoDataReaderTest {
    @Autowired
    CryptoDataReader reader;

    @Test
    void testGetAllSymbolData() {
        List<SymbolRecord> records = reader.getAllSymbolData(false);
        assertEquals(450,records.size(),"Expecting to get 450 records");
    }

    @Test
    void testGetAllSymbolDataWithReload() {
        List<SymbolRecord> records = reader.getAllSymbolData(true);
        assertEquals(450,records.size(),"Expecting to get 450 records");
    }
}