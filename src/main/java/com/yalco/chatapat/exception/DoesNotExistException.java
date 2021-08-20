package com.yalco.chatapat.exception;

public class DoesNotExistException extends ChatapatRuntimeException{
    public DoesNotExistException(String message) {
        super(message);
    }

    public DoesNotExistException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
