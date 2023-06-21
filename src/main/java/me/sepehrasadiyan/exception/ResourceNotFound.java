package me.sepehrasadiyan.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends BusinessAbstractException{
    public ResourceNotFound(String msg){
        super(msg, HttpStatus.NOT_FOUND);
    }
}
