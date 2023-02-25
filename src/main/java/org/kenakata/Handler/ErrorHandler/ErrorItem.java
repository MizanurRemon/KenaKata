package org.kenakata.Handler.ErrorHandler;

public class ErrorItem {
    private final int statusCode;
    private final String message;
  /*  private final HttpStatus httpStatus;

    //private final Throwable throwable;

    private final ZonedDateTime dateTime;*/


    public ErrorItem(int statusCode, String message) {
        this.message = message;
        //this.throwable = throwable;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }


/*
    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }*/
}
