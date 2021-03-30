package com.example.Exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateEntryException extends DataIntegrityViolationException {
    private static final long serialVersionUID=1L;

    public DuplicateEntryException(String msg) {
        super(msg);
    }
}
