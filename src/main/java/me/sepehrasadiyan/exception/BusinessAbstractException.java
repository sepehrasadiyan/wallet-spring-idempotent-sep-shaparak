package me.sepehrasadiyan.exception;

import org.springframework.http.HttpStatus;

public class BusinessAbstractException extends RuntimeException{

    private HttpStatus status;

    public BusinessAbstractException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
