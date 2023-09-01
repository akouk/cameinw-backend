package com.cameinw.cameinwbackend.exception;

public class CustomUserFriendlyException extends RuntimeException{
    public CustomUserFriendlyException(String message) {
        super(message);
    }
}
