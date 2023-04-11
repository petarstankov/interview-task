package com.xm.interviewtask.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String PRICE_INIT_ERROR = "Error initializing price data, check the logs for details.";

    private static final String SYMBOL_NOT_FOUND_ERROR = "No data for the requested criteria";
    @ExceptionHandler(value = DataInitializationException.class )
    protected ResponseEntity<Object> handleInitError(final RuntimeException ex, final WebRequest request) {

        return handleExceptionInternal(ex, PRICE_INIT_ERROR, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = SymbolDataNotFoundException.class)
    protected ResponseEntity<Object> handleSymbolNotFoundError(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, SYMBOL_NOT_FOUND_ERROR, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
