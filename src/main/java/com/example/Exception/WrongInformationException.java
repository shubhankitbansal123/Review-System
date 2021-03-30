package com.example.Exception;

public class WrongInformationException extends RuntimeException{
    private static final long serialVersionUID=1L;

    public WrongInformationException(String message) {
        super(message);
    }
}
