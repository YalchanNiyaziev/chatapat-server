package com.yalco.chatapat.exception;

public class ChatapatRuntimeException extends RuntimeException{

    public ChatapatRuntimeException(String message) {
        super(message);
    }

    public ChatapatRuntimeException(String message, Object... formatParams) {
        this(String.format(message, formatParams));
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
