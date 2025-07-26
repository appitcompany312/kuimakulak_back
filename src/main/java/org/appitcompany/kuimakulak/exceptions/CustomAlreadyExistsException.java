package org.appitcompany.kuimakulak.exceptions;

public class CustomAlreadyExistsException extends RuntimeException {
    public CustomAlreadyExistsException(String message) {
        super(message);
    }
}
