package com.yalco.chatapat.exception;

public class InternalErrorException extends ChatapatRuntimeException{
    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
