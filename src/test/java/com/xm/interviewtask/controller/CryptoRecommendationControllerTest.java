package com.xm.interviewtask.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CryptoRecommendationControllerTest {
    @Autowired
    private CryptoRecommendationController recommendationController;

    @Mock
    private MockHttpServletRequest servletRequest;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() {
        try {
            mockMvc
                    .perform(get("/api/v1/recommendations")
                            .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[?(@.symbol == 'BTC')].normalizedRange").value(0.4341211));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBySymbolOK() {
        try {
            mockMvc
                    .perform(get("/api/v1/recommendations/BTC")
                            .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[?(@.symbol == 'BTC')].normalizedRange").value(0.4341211));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBySymbolNotFound() {
        try {
            mockMvc
                    .perform(get("/api/v1/recommendations/BTCX")
                            .contentType("application/json"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getHighestByDay() {
        try {
            mockMvc
                    .perform(get("/api/v1/recommendations/highest?date=2022-01-06")
                            .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[?(@.symbol == 'ETH')].normalizedRange").value(0.054503407));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getHighestByDayNotFound() {
        try {
            mockMvc
                    .perform(get("/api/v1/recommendations/highest?date=2022-02-06")
                            .contentType("application/json"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}