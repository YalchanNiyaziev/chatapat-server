package com.yalco.chatapat.exception;

public class UserConnectionCreationException extends ChatapatRuntimeException{
    public UserConnectionCreationException(String message) {
        super(message);
    }

    public UserConnectionCreationException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
