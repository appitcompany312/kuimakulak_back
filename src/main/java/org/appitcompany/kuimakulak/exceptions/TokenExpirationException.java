package org.appitcompany.kuimakulak.exceptions;

public class TokenExpirationException extends RuntimeException {
    public TokenExpirationException(String message) {
        super(message);
    }
}
