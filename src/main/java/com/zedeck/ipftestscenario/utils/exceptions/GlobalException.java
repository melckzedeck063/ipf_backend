package com.zedeck.ipftestscenario.utils.exceptions;

public class GlobalException extends RuntimeException {
    private final int statusCode;

    public GlobalException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

