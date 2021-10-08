package com.yalco.chatapat.exception;

public class OperationNotAllowedException extends ChatapatRuntimeException{
    public OperationNotAllowedException(String message) {
        super(message);
    }

    public OperationNotAllowedException(String message, Object... formatParams) {
        super(message, formatParams);
    }
}
