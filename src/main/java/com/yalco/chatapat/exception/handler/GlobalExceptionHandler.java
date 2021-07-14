package com.yalco.chatapat.exception.handler;

import com.yalco.chatapat.exception.ChatapatRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class })
    protected void handleUserInputException(RuntimeException ex) {
        logger.warn(ex.getMessage());
        throw new ChatapatRuntimeException(ex.getMessage());
    }
}
