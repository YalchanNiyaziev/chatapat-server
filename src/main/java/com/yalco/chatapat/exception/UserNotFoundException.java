package com.yalco.chatapat.exception;

public class UserNotFoundException extends ChatapatRuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
