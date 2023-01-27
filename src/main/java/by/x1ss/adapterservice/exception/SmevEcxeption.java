package by.x1ss.adapterservice.exception;

import org.springframework.http.HttpStatus;

public class SmevEcxeption extends RuntimeException{
    private final HttpStatus httpStatus;

    public SmevEcxeption(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
