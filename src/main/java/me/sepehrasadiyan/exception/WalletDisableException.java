package me.sepehrasadiyan.exception;

import org.springframework.http.HttpStatus;

public class WalletDisableException extends BusinessAbstractException{
    public WalletDisableException(String message) {
        super(message, HttpStatus.LOCKED);
    }
}
