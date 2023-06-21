package me.sepehrasadiyan.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleException(ResourceNotFound ex){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage());
        return new ResponseEntity<ErrorMessage>(message , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleException(Exception e){
        LOGGER.error("Error:" ,e);
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "INTERNAL SERVER ERROR."
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WalletDisableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.LOCKED)
    public ResponseEntity<ErrorMessage> handelException(WalletDisableException e){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.LOCKED.value(),
                new Date(),
                e.getMessage()
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.LOCKED);
    }

    @ExceptionHandler(WalletAmountNotEnoughException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ResponseEntity<ErrorMessage> handelException(WalletAmountNotEnoughException e){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.PAYMENT_REQUIRED.value(),
                new Date(),
                e.getMessage()
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.PAYMENT_REQUIRED);
    }








}
