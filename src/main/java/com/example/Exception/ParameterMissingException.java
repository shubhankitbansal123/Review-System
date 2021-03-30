package com.example.Exception;

public class ParameterMissingException extends RuntimeException{

    private static final long serialVersionUID=1L;

    public ParameterMissingException(String message) {
        super(message);
    }
}
