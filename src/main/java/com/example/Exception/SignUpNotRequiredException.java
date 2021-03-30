package com.example.Exception;

public class SignUpNotRequiredException extends RuntimeException{

    private static final long serialVersionUID=1L;

    public SignUpNotRequiredException(String message) {
        super(message);
    }
}
