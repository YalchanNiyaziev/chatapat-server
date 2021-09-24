package com.yalco.chatapat.exception;

public class AccessDeniedException extends ChatapatRuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
