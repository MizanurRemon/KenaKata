package org.kenakata.Handler.ErrorHandler;

public class ApiRequestException extends RuntimeException {
    public ApiRequestException(String message) {
        super(message);
    }


}
