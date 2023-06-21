package me.sepehrasadiyan.exception;

import org.springframework.http.HttpStatus;

public class WalletAmountNotEnoughException extends BusinessAbstractException{
    public WalletAmountNotEnoughException(String message) {
        super(message, HttpStatus.PAYMENT_REQUIRED);
    }
}
