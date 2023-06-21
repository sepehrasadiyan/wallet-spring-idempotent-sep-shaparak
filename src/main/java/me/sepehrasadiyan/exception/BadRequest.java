package me.sepehrasadiyan.exception;

import org.springframework.http.HttpStatus;

public class BadRequest extends BusinessAbstractException{

    public BadRequest(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
