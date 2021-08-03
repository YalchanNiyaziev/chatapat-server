package com.yalco.chatapat.exception;

public class UserConnectionOperationException extends ChatapatRuntimeException{
    public UserConnectionOperationException(String message) {
        super(message);
    }

    public UserConnectionOperationException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
