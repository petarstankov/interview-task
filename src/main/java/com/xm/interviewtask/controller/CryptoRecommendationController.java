package com.xm.interviewtask.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xm.interviewtask.response.CryptoRecommendation;
import com.xm.interviewtask.service.CryptoRecommendationService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/recommendations")
public class CryptoRecommendationController {
    private final Map<String, LocalBucket> bucketCache = new ConcurrentHashMap<>();

    @Value("${rate.limit:20}")
    private Integer rateLimit;

    @Value("${rate.limit.duration:1}")
    private Integer rateLimitMinutes;

    @Autowired
    private CryptoRecommendationService cryptoRecommendationService;


    @GetMapping("")
    @ApiOperation("Get sorted recommendations based on normalized range from all available data")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 429, message = "Rate limit exceeded, wait a while before retrying"),
                    @ApiResponse(code = 500, message = "Unexpected exception occurred, check the logs for details")
            }
    )
    public ResponseEntity<List<CryptoRecommendation>> getAll(HttpServletRequest request) {
        if (checkRateLimit(request.getRemoteAddr())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        List<CryptoRecommendation> recommendations = cryptoRecommendationService.getAllRecommendations();
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping("/{symbol}")
    @ApiOperation("Get all available data for a specific symbol")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 404, message = "No data found for the requested symbol"),
                    @ApiResponse(code = 429, message = "Rate limit exceeded, wait a while before retrying"),
                    @ApiResponse(code = 500, message = "Unexpected exception occurred, check the logs for details")
                }
    )
    public ResponseEntity<CryptoRecommendation> getBySymbol(
            @PathVariable("symbol")
            @ApiParam(value = "Symbol to fetch data for", example = "BTC")
            String symbol,HttpServletRequest request) {
        if (checkRateLimit(request.getRemoteAddr())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        CryptoRecommendation symbolData = cryptoRecommendationService.getRecommendationBySymbol(symbol);
        return new ResponseEntity<>(symbolData, HttpStatus.OK);
    }

    @GetMapping("/highest")
    @ApiOperation("Get the symbol with the highest normalized value for a given date")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 404, message = "No data found for the requested date"),
                    @ApiResponse(code = 429, message = "Rate limit exceeded, wait a while before retrying"),
                    @ApiResponse(code = 500, message = "Unexpected exception occurred, check the logs for details")
                }
            )
    public ResponseEntity<CryptoRecommendation> getHighestByDay(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value = "Date for which we want the symbol with highest normalized value in ISO-8601 Format", example = "2022-01-18")
            LocalDate date,
            HttpServletRequest request) {
        if (checkRateLimit(request.getRemoteAddr())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        CryptoRecommendation recommendation = cryptoRecommendationService.getHighestRecommendationByDay(date);

        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }

    private boolean checkRateLimit(String ipAddress) {
        LocalBucket bucket = bucketCache.computeIfAbsent(ipAddress, this::createNewBucket);

        return !bucket.tryConsume(1);
    }

    private LocalBucket createNewBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.classic(rateLimit, Refill.greedy(rateLimit, Duration.ofMinutes(rateLimitMinutes)));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
